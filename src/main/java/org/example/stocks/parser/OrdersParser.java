package org.example.stocks.parser;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.stocks.enums.OrderType;
import org.example.stocks.exceptions.FileNotFoundException;
import org.example.stocks.exceptions.OrderParseException;
import org.example.stocks.types.Order;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class OrdersParser implements IOrdersParser {

    private final String filePath;

    public List<Order> parse() {
        List<Order> parsedOrders = new ArrayList<>();
        for (String line : parseFile()) {
            parsedOrders.add(parseOrder(line));
        }
        return parsedOrders;
    }

    private Order parseOrder(String line) {
        try {
            String[] split = line.split("\\s+");
            return Order.builder()
                    .orderId(split[0])
                    .time(split[1])
                    .stock(split[2])
                    .orderType(OrderType.valueOf(split[3].toUpperCase()))
                    .price(Double.parseDouble(split[4]))
                    .quantity(Integer.parseInt(split[5]))
                    .build();
        } catch (Exception e) {
            throw new OrderParseException(line, e);
        }
    }

    @SneakyThrows
    private List<String> parseFile() {
        List<String> contents = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(fileInputStream);
        while (scanner.hasNext()) {
            contents.add(scanner.nextLine());
        }
        return contents;
    }
}
