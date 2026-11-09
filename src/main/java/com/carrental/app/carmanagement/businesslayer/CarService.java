package com.carrental.app.carmanagement.businesslayer;

import com.carrental.app.carmanagement.domainlayer.Car;
import com.carrental.app.carmanagement.domainlayer.CarStatus;
import com.carrental.app.exception.CarNotFoundException;

import java.util.List;

public interface CarService {

    List<Car> getAllCars();

    List<Car> getCarsByStatus(CarStatus status);

    Car getCarByCarId(String carId) throws CarNotFoundException;

    Car addCar(Car car);

    Car updateCar(String carId, Car updatedCar) throws CarNotFoundException;

    void deleteByCarId(String carId) throws CarNotFoundException;
}
