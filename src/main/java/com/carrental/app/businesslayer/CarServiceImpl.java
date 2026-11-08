package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.CarStatus;
import com.carrental.app.domainlayer.RentalStatus;
import com.carrental.app.dataaccesslayer.CarRepository;
import com.carrental.app.exception.BadRequestException;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.exception.ConflictException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CarServiceImpl implements CarService {
    private static final int OLDEST_YEAR = 1886;
    private static final int NEWEST_YEAR = 2100;

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    @Override
    public Car getCarByCarId(String carId) throws CarNotFoundException {
        return carRepository.findByCarId(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
    }

    @Override
    public Car addCar(Car car) {
        validateCar(car);

        car.setId(null);                             // null = new, the database generates the id
        car.setCarId(UUID.randomUUID().toString());  // system: public id
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(String carId, Car updatedCar) throws CarNotFoundException {
        validateCar(updatedCar);
        Car existingCar = getCarByCarId(carId);
        validateStatusChange(existingCar, updatedCar);

        // Preserve everything the client cannot change:
        // the ids, and the car's rentals (otherwise saving an empty list would delete them).
        updatedCar.setId(existingCar.getId());
        updatedCar.setCarId(existingCar.getCarId());
        updatedCar.setRentals(existingCar.getRentals());

        return carRepository.save(updatedCar);
    }

    @Override
    public void deleteByCarId(String carId) throws CarNotFoundException {
        Car car = getCarByCarId(carId);   // 404 if it doesn't exist
        // the car's rentals are deleted too (cascade on Car.rentals)
        carRepository.delete(car);
    }

    @Override
    public boolean existsByCarId(String carId) {
        return carRepository.existsByCarId(carId);
    }

    // ---------------------------------------------------------------------
    // Validation and business rules
    // ---------------------------------------------------------------------

    private void validateCar(Car car) {
        if (car == null) {
            throw new BadRequestException("Car cannot be null.");
        }

        if (car.getMake() == null || car.getMake().isBlank()) {
            throw new BadRequestException("Make is required.");
        }

        if (car.getModel() == null || car.getModel().isBlank()) {
            throw new BadRequestException("Model is required.");
        }

        if (car.getYear() < OLDEST_YEAR || car.getYear() > NEWEST_YEAR) {
            throw new BadRequestException("Year must be between " + OLDEST_YEAR + " and " + NEWEST_YEAR + ".");
        }

        if (car.getDailyRate() == null || car.getDailyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Daily rate must be greater than 0.");
        }

        if (car.getStatus() == null) {
            throw new BadRequestException("Status is required.");
        }
    }

    // Business rule: a car that is currently out on an ACTIVE rental cannot be put into maintenance.
    private void validateStatusChange(Car existingCar, Car updatedCar) {
        boolean goingToMaintenance = existingCar.getStatus() != CarStatus.MAINTENANCE
                && updatedCar.getStatus() == CarStatus.MAINTENANCE;

        boolean hasActiveRental = existingCar.getRentals().stream()
                .anyMatch(rental -> rental.getStatus() == RentalStatus.ACTIVE);

        if (goingToMaintenance && hasActiveRental) {
            throw new ConflictException(
                    "Car " + existingCar.getCarId() + " is on an active rental and cannot be put into maintenance.");
        }
    }
}
