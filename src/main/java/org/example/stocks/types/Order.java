package org.example.stocks.types;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.example.stocks.enums.OrderType;

@Getter
@Builder
@ToString
public class Order {

    String orderId;
    String time;
    String stock;
    OrderType orderType;
    Double price;
    Integer quantity;

    public void reduceQuantity(int reduceBy) {
        quantity -= reduceBy;
    }
}
