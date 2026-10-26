package com.carrental.app.repository;

import com.carrental.app.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByCustomerId(Long customerId);

    List<Rental> findByCarId(Long carId);
}
