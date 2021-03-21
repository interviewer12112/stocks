package org.example.stocks.logic;

import org.example.stocks.enums.SortField;
import org.example.stocks.types.Order;
import org.example.stocks.types.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class OrdersQueue {

    public final Map<String, PriorityQueue<Order>> queues;
    private final Sort[] sorts;

    public OrdersQueue(Sort... sorts) {
        queues = new HashMap<>();
        this.sorts = sorts;
    }

    public void add(Order order) {
        if (!queues.containsKey(order.getStock())) {
            queues.put(order.getStock(), getNewQueue());
        }
        queues.get(order.getStock()).add(order);
    }

    public Order poll(String stock) {
        if (!queues.containsKey(stock)) {
            return null;
        }
        return queues.get(stock).poll();
    }

    public Order peek(String stock) {
        if (!queues.containsKey(stock)) {
            return null;
        }
        return queues.get(stock).peek();
    }

    private PriorityQueue<Order> getNewQueue() {
        return new PriorityQueue<>((o1, o2) -> {
            for (Sort sort : sorts) {
                int c = compare(o1, o2, sort.getField());
                if (c != 0) {
                    switch (sort.getOrder()) {
                        case ASC: return c;
                        case DESC: return -c;
                    }
                }
            }
            return 0;
        });
    }

    private int compare(Order o1, Order o2, SortField field) {
        switch (field) {
            case TIME: {
                return o1.getTime().compareTo(o2.getTime());
            }
            case PRICE: {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        }
        return 0;
    }
}
