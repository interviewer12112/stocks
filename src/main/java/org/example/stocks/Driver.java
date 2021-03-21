package org.example.stocks;

import org.example.stocks.dao.IOrderStore;
import org.example.stocks.dao.InMemoryOrderStore;
import org.example.stocks.logic.OrderMatcher;
import org.example.stocks.parser.IOrdersParser;
import org.example.stocks.parser.OrdersParser;
import org.example.stocks.types.Order;
import org.example.stocks.types.Trade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class Driver {

    private final IOrdersParser ordersParser;
    private final OrderMatcher orderMatcher;
    private final IOrderStore orderStore;

    public Driver(OrderMatcher orderMatcher, IOrderStore orderStore) {
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        System.out.println();
        ordersParser = new OrdersParser(filePath);
        this.orderMatcher = orderMatcher;
        this.orderStore = orderStore;
    }

    public void run() {
        List<Order> orders = ordersParser.parse();
        List<Trade> allTrades = new ArrayList<>();
        for (Order order : orders) {
            List<Trade> trades = orderMatcher.match(order);
            allTrades.addAll(trades);
            if (order.getQuantity() > 0) {
                orderStore.addOrder(order);
            }
        }
        for (Trade trade: allTrades) {
            System.out.println(trade);
        }
    }
}
