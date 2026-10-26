package com.carrental.app.exception;

// parent class for all "not found" errors -> mapped to 404
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
