package org.example.stocks.exceptions;

public class OrderParseException extends RuntimeException {

    public OrderParseException(String line, Throwable t) {
        super(String.format("Couldn't parse order from line: %s", line), t);
    }
}
