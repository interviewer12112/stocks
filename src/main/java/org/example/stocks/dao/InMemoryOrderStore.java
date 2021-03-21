package org.example.stocks.dao;

import org.example.stocks.enums.SortField;
import org.example.stocks.enums.SortOrder;
import org.example.stocks.logic.OrdersQueue;
import org.example.stocks.types.Order;
import org.example.stocks.types.Sort;

public class InMemoryOrderStore implements IOrderStore {

    private final OrdersQueue buyQueues;
    private final OrdersQueue sellQueues;

    public InMemoryOrderStore() {
        buyQueues = new OrdersQueue(
                new Sort(SortField.PRICE, SortOrder.DESC),
                new Sort(SortField.TIME, SortOrder.ASC)
        );
        sellQueues = new OrdersQueue(
                new Sort(SortField.PRICE, SortOrder.ASC),
                new Sort(SortField.TIME, SortOrder.ASC)
        );
    }

    public Order pollBuy(String stock) {
        return buyQueues.poll(stock);
    }

    public Order pollSell(String stock) {
        return sellQueues.poll(stock);
    }

    public boolean addOrder(Order order) {
        switch (order.getOrderType()) {
            case BUY:
                return processBuy(order);
            case SELL:
                return processSell(order);
        }
        return true;
    }

    private boolean processBuy(Order order) {
        buyQueues.add(order);
        return true;
    }

    private boolean processSell(Order order) {
        sellQueues.add(order);
        return true;
    }
}
