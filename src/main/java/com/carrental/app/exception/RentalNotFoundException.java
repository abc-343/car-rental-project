package com.carrental.app.exception;

public class RentalNotFoundException extends NotFoundException {

    public RentalNotFoundException(String rentalId) {
        super("Rental " + rentalId + " not found");
    }
}
