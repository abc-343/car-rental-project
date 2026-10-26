package com.carrental.app.exception;

// used for business rule errors (ex: end date before start date) -> mapped to 400
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
