package com.carrental.app.exception;

public class CarNotFoundException extends NotFoundException {

    public CarNotFoundException(String carId) {
        super("Car " + carId + " not found");
    }
}
