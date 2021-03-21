package org.example.stocks.logic;

import org.example.stocks.types.Order;
import org.example.stocks.types.Trade;

import java.util.List;

public interface IOrderMatcher {
    List<Trade> match(Order order);
}
