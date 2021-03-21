package org.example.stocks.logic;

import lombok.RequiredArgsConstructor;
import org.example.stocks.dao.IOrderStore;
import org.example.stocks.enums.OrderType;
import org.example.stocks.types.Order;
import org.example.stocks.types.Trade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderMatcher implements IOrderMatcher {

    private final IOrderStore orderStore;

    public List<Trade> match(Order order) {
        if (order.getOrderType() == OrderType.BUY) {
            return matchBuyOrder(order);
        } else {
            return matchSellOrder(order);
        }
    }

    public List<Trade> matchBuyOrder(Order buyOrder) {
        List<Trade> trades = new ArrayList<>();
        while (true) {
            if (buyOrder.getQuantity() == 0) {
                return trades;
            }
            Order sellOrder = orderStore.pollSell(buyOrder.getStock());
            if (Objects.isNull(sellOrder) || sellOrder.getPrice() > buyOrder.getPrice()) {
                return trades;
            }
            int qty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
            trades.add(
                    Trade.builder()
                            .buyOrderId(buyOrder.getOrderId())
                            .sellOrderId(sellOrder.getOrderId())
                            .quantity(qty)
                            .sellPrice(sellOrder.getPrice())
                            .build()
            );
            sellOrder.reduceQuantity(qty);
            buyOrder.reduceQuantity(qty);
            if (sellOrder.getQuantity() > 0) {
                orderStore.addOrder(sellOrder);
            }
        }
    }

    public List<Trade> matchSellOrder(Order sellOrder) {
        List<Trade> trades = new ArrayList<>();
        while (true) {
            if (sellOrder.getQuantity() == 0) {
                return trades;
            }
            Order buyOrder = orderStore.pollBuy(sellOrder.getStock());
            if (Objects.isNull(buyOrder) || sellOrder.getPrice() > buyOrder.getPrice()) {
                return trades;
            }
            int qty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
            trades.add(
                    Trade.builder()
                            .buyOrderId(buyOrder.getOrderId())
                            .sellOrderId(sellOrder.getOrderId())
                            .quantity(qty)
                            .sellPrice(sellOrder.getPrice())
                            .build()
            );
            buyOrder.reduceQuantity(qty);
            sellOrder.reduceQuantity(qty);
            if (buyOrder.getQuantity() > 0) {
                orderStore.addOrder(buyOrder);
            }
        }
    }
}
