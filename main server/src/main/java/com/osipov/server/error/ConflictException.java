package com.osipov.server.error;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}