package com.carrental.app.exception;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(Long id) {
        super("Customer " + id + " not found");
    }
}
