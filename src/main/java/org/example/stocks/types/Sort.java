package org.example.stocks.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stocks.enums.SortField;
import org.example.stocks.enums.SortOrder;

@Getter
@AllArgsConstructor
public class Sort {
    SortField field;
    SortOrder order;
}
