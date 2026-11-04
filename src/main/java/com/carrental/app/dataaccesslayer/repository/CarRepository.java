package com.carrental.app.dataaccesslayer.repository;

import com.carrental.app.dataaccesslayer.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
