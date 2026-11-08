package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Rental;
import com.carrental.app.domainlayer.RentalStatus;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.exception.RentalNotFoundException;

import java.util.List;

public interface RentalService {

    List<Rental> getAllRentals();

    List<Rental> getRentalsByStatus(RentalStatus status);

    Rental getRentalByRentalId(String rentalId) throws RentalNotFoundException;

    Rental addRental(Rental rental) throws CustomerNotFoundException, CarNotFoundException;

    Rental updateRental(String rentalId, Rental updatedRental)
            throws RentalNotFoundException, CustomerNotFoundException, CarNotFoundException;

    void deleteByRentalId(String rentalId) throws RentalNotFoundException;
}
