package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.CarStatus;
import com.carrental.app.domainlayer.Customer;
import com.carrental.app.domainlayer.Rental;
import com.carrental.app.domainlayer.RentalStatus;
import com.carrental.app.dataaccesslayer.CarRepository;
import com.carrental.app.dataaccesslayer.CustomerRepository;
import com.carrental.app.dataaccesslayer.RentalRepository;
import com.carrental.app.exception.BadRequestException;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.exception.ConflictException;
import com.carrental.app.exception.CustomerNotFoundException;
import com.carrental.app.exception.RentalNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {

    private static final List<RentalStatus> BLOCKING_STATUSES = List.of(RentalStatus.RESERVED, RentalStatus.ACTIVE);

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    public RentalServiceImpl(RentalRepository rentalRepository,
                             CustomerRepository customerRepository,
                             CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> getRentalsByStatus(RentalStatus status) {
        return rentalRepository.findByStatus(status);
    }

    @Override
    public Rental getRentalByRentalId(String rentalId) throws RentalNotFoundException {
        return rentalRepository.findByRentalId(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    @Override
    public Rental addRental(Rental rental) throws CustomerNotFoundException, CarNotFoundException {
        validateRental(rental);

        // the mapper only filled in the public ids, load the real customer and car
        Customer customer = findCustomer(rental.getCustomer().getCustomerId());
        Car car = findCar(rental.getCar().getCarId());
        validateCarCanBeBooked(car, rental, null);

        rental.setId(null);                                       // null = new
        rental.setRentalId(UUID.randomUUID().toString());         // system: public id
        rental.setCustomer(customer);
        rental.setCar(car);
        rental.setTotalCost(calculateTotalCost(car.getDailyRate(), rental.getStartDate(), rental.getEndDate()));  // system
        return rentalRepository.save(rental);
    }

    @Override
    public Rental updateRental(String rentalId, Rental updatedRental)
            throws RentalNotFoundException, CustomerNotFoundException, CarNotFoundException {
        validateRental(updatedRental);
        Rental existingRental = getRentalByRentalId(rentalId);   // 404 if the rental doesn't exist

        Customer customer = findCustomer(updatedRental.getCustomer().getCustomerId());
        Car car = findCar(updatedRental.getCar().getCarId());
        validateCarCanBeBooked(car, updatedRental, existingRental.getId());

        // Preserve the ids, the client cannot change them
        updatedRental.setId(existingRental.getId());
        updatedRental.setRentalId(existingRental.getRentalId());
        updatedRental.setCustomer(customer);
        updatedRental.setCar(car);
        // the total is always recalculated, the client can't set it
        updatedRental.setTotalCost(calculateTotalCost(car.getDailyRate(), updatedRental.getStartDate(), updatedRental.getEndDate()));
        return rentalRepository.save(updatedRental);
    }

    @Override
    public void deleteByRentalId(String rentalId) throws RentalNotFoundException {
        Rental rental = getRentalByRentalId(rentalId);   // 404 if it doesn't exist
        rentalRepository.delete(rental);
    }

    // ---------------------------------------------------------------------
    // Validation and business rules
    // ---------------------------------------------------------------------

    private void validateRental(Rental rental) {
        if (rental == null) {
            throw new BadRequestException("Rental cannot be null.");
        }

        if (rental.getCustomer() == null || rental.getCustomer().getCustomerId() == null) {
            throw new BadRequestException("Customer id is required.");
        }

        if (rental.getCar() == null || rental.getCar().getCarId() == null) {
            throw new BadRequestException("Car id is required.");
        }

        if (rental.getStartDate() == null || rental.getEndDate() == null) {
            throw new BadRequestException("Start date and end date are required.");
        }

        // Business rule: a rental cannot end before it starts.
        if (rental.getEndDate().isBefore(rental.getStartDate())) {
            throw new BadRequestException("endDate must not be before startDate");
        }

        if (rental.getStatus() == null) {
            throw new BadRequestException("Status is required.");
        }
    }

    // Business rules for rentals that hold the car (RESERVED or ACTIVE):
    //  - a car in maintenance cannot be rented
    //  - the same car cannot be booked twice for overlapping dates
    // ignoreRentalId is the (internal) id of the rental being updated, so it doesn't conflict with itself.
    private void validateCarCanBeBooked(Car car, Rental rental, Long ignoreRentalId) {
        if (!BLOCKING_STATUSES.contains(rental.getStatus())) {
            return;   // COMPLETED and CANCELLED rentals are just history
        }

        if (car.getStatus() == CarStatus.MAINTENANCE) {
            throw new BadRequestException("Car " + car.getCarId() + " is in maintenance and cannot be rented");
        }

        boolean overlaps = rentalRepository.findByCarAndStatusIn(car, BLOCKING_STATUSES).stream()
                .filter(other -> !Objects.equals(other.getId(), ignoreRentalId))
                .anyMatch(other -> !other.getStartDate().isAfter(rental.getEndDate())
                        && !other.getEndDate().isBefore(rental.getStartDate()));

        if (overlaps) {
            throw new ConflictException("Car " + car.getCarId() + " is already booked for some of these dates");
        }
    }

    // Business rule: total = number of days * daily rate. A same-day rental counts as 1 day.
    private BigDecimal calculateTotalCost(BigDecimal dailyRate, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days == 0) {
            days = 1;
        }
        return dailyRate.multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_UP);
    }

    private Customer findCustomer(String customerId) throws CustomerNotFoundException {
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private Car findCar(String carId) throws CarNotFoundException {
        return carRepository.findByCarId(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
    }
}
