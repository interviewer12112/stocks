package org.example.stocks.logic;

import lombok.RequiredArgsConstructor;
import org.example.stocks.dao.IOrderBook;
import org.example.stocks.enums.OrderType;
import org.example.stocks.types.Order;
import org.example.stocks.types.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class OrderMatcher implements IOrderMatcher {

    private final IOrderBook orderBook;

    @Override
    public List<Trade> match(Order order) {
        List<Trade> trades = new ArrayList<>();
        while (true) {
            if (order.getQuantity() == 0) {
                return trades;
            }
            Trade trade = order.getOrderType() == OrderType.BUY ? matchBuyOrder(order) : matchSellOrder(order);
            if (Objects.isNull(trade)) {
                return trades;
            }
            trades.add(trade);
        }
    }

    public Trade matchBuyOrder(Order buyOrder) {
        Order sellOrder = orderBook.pollSell(buyOrder.getStock());
        if (Objects.isNull(sellOrder)) {
            return null;
        }
        Trade trade = matchOrders(buyOrder, sellOrder);
        if (sellOrder.getQuantity() > 0) {
            orderBook.addOrder(sellOrder); // push back to order book if not completely fulfilled
        }
        return trade;
    }

    public Trade matchSellOrder(Order sellOrder) {
        Order buyOrder = orderBook.pollBuy(sellOrder.getStock());
        if (Objects.isNull(buyOrder)) {
            return null;
        }
        Trade trade = matchOrders(buyOrder, sellOrder);
        if (buyOrder.getQuantity() > 0) {
            orderBook.addOrder(buyOrder); // push back to order book if not completely fulfilled
        }
        return trade;
    }

    private Trade matchOrders(Order buyOrder, Order sellOrder) {
        if (Objects.isNull(buyOrder) || Objects.isNull(sellOrder)) {
            return null;
        }
        if (sellOrder.getPrice() > buyOrder.getPrice()) {
            return null;
        }
        int qty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
        buyOrder.reduceQuantity(qty);
        sellOrder.reduceQuantity(qty);
        return Trade.builder()
                .buyOrderId(buyOrder.getOrderId())
                .sellOrderId(sellOrder.getOrderId())
                .quantity(qty)
                .sellPrice(sellOrder.getPrice())
                .build();
    }
}
