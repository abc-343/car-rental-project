package com.carrental.app.exception;

public class CarNotFoundException extends NotFoundException {

    public CarNotFoundException(Long id) {
        super("Car " + id + " not found");
    }
}
