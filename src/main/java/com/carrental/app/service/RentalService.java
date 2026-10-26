package com.carrental.app.service;

import com.carrental.app.dto.request.RentalRequestDTO;
import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.dto.response.RentalWithDetailsResponseDTO;
import com.carrental.app.entity.Car;
import com.carrental.app.entity.CarStatus;
import com.carrental.app.entity.Customer;
import com.carrental.app.entity.Rental;
import com.carrental.app.exception.BadRequestException;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.exception.RentalNotFoundException;
import com.carrental.app.mapper.RentalMapper;
import com.carrental.app.repository.CarRepository;
import com.carrental.app.repository.CustomerRepository;
import com.carrental.app.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    public RentalService(RentalRepository rentalRepository,
                         CustomerRepository customerRepository,
                         CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    public List<RentalResponseDTO> findAll() {
        return rentalRepository.findAll().stream()
                .map(RentalMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RentalResponseDTO findById(Long id) {
        return RentalMapper.toResponse(getRentalOrThrow(id));
    }

    @Transactional(readOnly = true)
    public RentalWithDetailsResponseDTO findRentalWithDetails(Long id) {
        return RentalMapper.toWithDetails(getRentalOrThrow(id));
    }

    @Transactional
    public RentalResponseDTO create(RentalRequestDTO request) {
        Rental rental = new Rental();
        fillRental(rental, request);
        return RentalMapper.toResponse(rentalRepository.save(rental));
    }

    @Transactional
    public RentalResponseDTO update(Long id, RentalRequestDTO request) {
        Rental rental = getRentalOrThrow(id);
        fillRental(rental, request);
        return RentalMapper.toResponse(rentalRepository.save(rental));
    }

    @Transactional
    public void delete(Long id) {
        Rental rental = getRentalOrThrow(id);
        rentalRepository.delete(rental);
    }

    // business rule: number of days * daily rate. A same-day rental counts as 1 day
    public BigDecimal calculateTotalCost(BigDecimal dailyRate, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days == 0) {
            days = 1;
        }
        return dailyRate.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }

    // shared by create and update: checks the rules and copies the values onto the rental
    private void fillRental(Rental rental, RentalRequestDTO request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("endDate must not be before startDate");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.customerId()));
        Car car = carRepository.findById(request.carId())
                .orElseThrow(() -> new CarNotFoundException(request.carId()));

        if (car.getStatus() == CarStatus.MAINTENANCE) {
            throw new BadRequestException("Car " + car.getId() + " is in maintenance and cannot be rented");
        }

        rental.setCustomer(customer);
        rental.setCar(car);
        rental.setStartDate(request.startDate());
        rental.setEndDate(request.endDate());
        rental.setStatus(request.status());
        rental.setTotalCost(calculateTotalCost(car.getDailyRate(), request.startDate(), request.endDate()));
    }

    private Rental getRentalOrThrow(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }
}
