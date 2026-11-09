package com.carrental.app.carmanagement.dataaccesslayer;

import com.carrental.app.carmanagement.domainlayer.Car;
import com.carrental.app.carmanagement.domainlayer.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    // carId is the public id, e.g. CAR-0001 (the Long id stays internal)
    Optional<Car> findByCarId(String carId);

    List<Car> findByStatus(CarStatus status);
}
