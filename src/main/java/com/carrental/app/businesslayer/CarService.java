package com.carrental.app.businesslayer;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.CarStatus;
import com.carrental.app.exception.CarNotFoundException;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    List<Car> getCarsByStatus(CarStatus status);

    Car getCarByCarId(String carId) throws CarNotFoundException;

    Car addCar(Car car);

    Car updateCar(String carId, Car updatedCar) throws CarNotFoundException;

    void deleteByCarId(String carId) throws CarNotFoundException;

    boolean existsByCarId(String carId);
}
