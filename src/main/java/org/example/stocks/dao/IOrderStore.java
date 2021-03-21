package org.example.stocks.dao;

import org.example.stocks.types.Order;

public interface IOrderStore {

    Order pollBuy(String stock);
    Order pollSell(String stock);
    boolean addOrder(Order order);
}
