package org.example.stocks.dao;

import org.example.stocks.types.Order;

public interface IOrderBook {

    Order pollBuy(String stock);
    Order pollSell(String stock);
    boolean addOrder(Order order);
}
