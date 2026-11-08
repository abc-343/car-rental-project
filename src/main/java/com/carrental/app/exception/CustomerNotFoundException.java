package com.carrental.app.exception;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(String customerId) {
        super("Customer " + customerId + " not found");
    }
}
