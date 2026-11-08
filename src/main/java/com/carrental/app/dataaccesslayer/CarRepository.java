package com.carrental.app.dataaccesslayer;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    // carId is the public UUID (the Long id stays internal)
    Optional<Car> findByCarId(String carId);

    List<Car> findByStatus(CarStatus status);
}
