package org.example.stocks.exceptions;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String path) {
        super(String.format("No file exists at path: %s", path));
    }
}
