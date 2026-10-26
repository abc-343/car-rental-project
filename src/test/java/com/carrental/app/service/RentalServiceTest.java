package com.carrental.app.service;

import com.carrental.app.dto.request.RentalRequestDTO;
import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.entity.*;
import com.carrental.app.exception.BadRequestException;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.exception.RentalNotFoundException;
import com.carrental.app.repository.CarRepository;
import com.carrental.app.repository.CustomerRepository;
import com.carrental.app.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// unit tests: the repositories are mocked, no database is used
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private RentalService rentalService;

    private Customer customer;
    private Car car;

    @BeforeEach
    void setUp() {
        customer = new Customer("John", "Smith", "john.smith@email.com", "514-555-0101");
        customer.setId(1L);

        car = new Car("Toyota", "Corolla", 2024, new BigDecimal("59.99"), CarStatus.AVAILABLE);
        car.setId(10L);
    }

    @Test
    void calculateTotalCost_multipliesDaysByDailyRate() {
        BigDecimal total = rentalService.calculateTotalCost(new BigDecimal("59.99"),
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5));

        assertEquals(new BigDecimal("239.96"), total);
    }

    @Test
    void calculateTotalCost_sameDayCountsAsOneDay() {
        BigDecimal total = rentalService.calculateTotalCost(new BigDecimal("59.99"),
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 1));

        assertEquals(new BigDecimal("59.99"), total);
    }

    @Test
    void create_validRequest_savesRentalWithCalculatedCost() {
        RentalRequestDTO request = new RentalRequestDTO(1L, 10L,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), RentalStatus.RESERVED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental rental = invocation.getArgument(0);
            rental.setId(100L);
            return rental;
        });

        RentalResponseDTO response = rentalService.create(request);

        assertEquals(100L, response.id());
        assertEquals(1L, response.customerId());
        assertEquals(10L, response.carId());
        assertEquals(new BigDecimal("239.96"), response.totalCost());
        assertEquals(RentalStatus.RESERVED, response.status());
    }

    @Test
    void create_endDateBeforeStartDate_throwsBadRequest() {
        RentalRequestDTO request = new RentalRequestDTO(1L, 10L,
                LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 1), RentalStatus.RESERVED);

        assertThrows(BadRequestException.class, () -> rentalService.create(request));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void create_unknownCustomer_throwsNotFound() {
        RentalRequestDTO request = new RentalRequestDTO(99L, 10L,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), RentalStatus.RESERVED);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> rentalService.create(request));
    }

    @Test
    void create_unknownCar_throwsNotFound() {
        RentalRequestDTO request = new RentalRequestDTO(1L, 99L,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), RentalStatus.RESERVED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> rentalService.create(request));
    }

    @Test
    void create_carInMaintenance_throwsBadRequest() {
        car.setStatus(CarStatus.MAINTENANCE);
        RentalRequestDTO request = new RentalRequestDTO(1L, 10L,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), RentalStatus.RESERVED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(10L)).thenReturn(Optional.of(car));

        assertThrows(BadRequestException.class, () -> rentalService.create(request));
    }

    @Test
    void findById_missingRental_throwsNotFound() {
        when(rentalRepository.findById(123L)).thenReturn(Optional.empty());

        RentalNotFoundException ex = assertThrows(RentalNotFoundException.class,
                () -> rentalService.findById(123L));
        assertEquals("Rental 123 not found", ex.getMessage());
    }
}
