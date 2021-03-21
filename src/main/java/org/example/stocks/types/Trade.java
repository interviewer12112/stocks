package org.example.stocks.types;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Trade {

    String buyOrderId;
    String sellOrderId;
    double sellPrice;
    int quantity;
}
