package com.carrental.app.exception;

public class RentalNotFoundException extends NotFoundException {

    public RentalNotFoundException(Long id) {
        super("Rental " + id + " not found");
    }
}
