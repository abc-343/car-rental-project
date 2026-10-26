package com.carrental.app.exception;

// used when data already exists (ex: duplicate email) -> mapped to 409
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
