package org.example.stocks.parser;

import org.example.stocks.types.Order;

import java.util.List;

public interface IOrdersParser {

    List<Order> parse();
}
