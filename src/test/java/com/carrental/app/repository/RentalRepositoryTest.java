package com.carrental.app.repository;

import com.carrental.app.entity.Rental;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// uses the real H2 database with the data from data.sql
@DataJpaTest
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void seedData_hasAtLeastTenRowsInEachTable() {
        assertTrue(carRepository.count() >= 10);
        assertTrue(customerRepository.count() >= 10);
        assertTrue(rentalRepository.count() >= 10);
    }

    @Test
    void findByCustomerId_returnsOnlyThatCustomersRentals() {
        List<Rental> rentals = rentalRepository.findByCustomerId(1L);

        assertEquals(2, rentals.size());
        rentals.forEach(rental -> assertEquals(1L, rental.getCustomer().getId()));
    }

    @Test
    void findByCarId_returnsOnlyThatCarsRentals() {
        List<Rental> rentals = rentalRepository.findByCarId(1L);

        assertEquals(2, rentals.size());
        rentals.forEach(rental -> assertEquals(1L, rental.getCar().getId()));
    }

    @Test
    void existsByEmail_findsSeededCustomer() {
        assertTrue(customerRepository.existsByEmail("john.smith@email.com"));
        assertFalse(customerRepository.existsByEmail("nobody@email.com"));
    }
}
