package com.carrental.app.dataaccesslayer;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.Rental;
import com.carrental.app.domainlayer.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    // rentalId is the public UUID (the Long id stays internal)
    Optional<Rental> findByRentalId(String rentalId);

    List<Rental> findByStatus(RentalStatus status);

    // rentals of one car that are in one of the given statuses (used for the double-booking rule)
    List<Rental> findByCarAndStatusIn(Car car, List<RentalStatus> statuses);
}
