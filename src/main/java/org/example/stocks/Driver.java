package org.example.stocks;

import org.example.stocks.dao.IOrderStore;
import org.example.stocks.dao.InMemoryOrderStore;
import org.example.stocks.logic.OrderMatcher;
import org.example.stocks.parser.IOrdersParser;
import org.example.stocks.parser.OrdersParser;
import org.example.stocks.types.Order;
import org.example.stocks.types.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {

    private final IOrdersParser ordersParser;
    private final OrderMatcher orderMatcher;
    private final IOrderStore orderStore;

    public Driver() {
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        ordersParser = new OrdersParser(filePath);
        orderStore = new InMemoryOrderStore();
        orderMatcher = new OrderMatcher(orderStore);
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
            System.out.println(String.format("%s %.2f %d %s", trade.getBuyOrderId(), trade.getSellPrice(), trade.getQuantity(), trade.getSellOrderId()));
        }
    }
}
