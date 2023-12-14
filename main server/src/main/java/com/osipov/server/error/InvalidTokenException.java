package com.osipov.server.error;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Incorrect token");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}