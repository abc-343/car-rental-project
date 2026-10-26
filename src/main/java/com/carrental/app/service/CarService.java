package com.carrental.app.service;

import com.carrental.app.dto.request.CarRequestDTO;
import com.carrental.app.dto.response.CarResponseDTO;
import com.carrental.app.dto.response.CarWithRentalsResponseDTO;
import com.carrental.app.entity.Car;
import com.carrental.app.exception.CarNotFoundException;
import com.carrental.app.mapper.CarMapper;
import com.carrental.app.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    public List<CarResponseDTO> findAll() {
        return carRepository.findAll().stream()
                .map(CarMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CarResponseDTO findById(Long id) {
        return CarMapper.toResponse(getCarOrThrow(id));
    }

    @Transactional(readOnly = true)
    public CarWithRentalsResponseDTO findCarWithRentals(Long id) {
        return CarMapper.toWithRentals(getCarOrThrow(id));
    }

    @Transactional
    public CarResponseDTO create(CarRequestDTO request) {
        Car car = CarMapper.toEntity(request);
        Car saved = carRepository.save(car);
        return CarMapper.toResponse(saved);
    }

    // PUT replaces every field, so calling it twice gives the same result (idempotent)
    @Transactional
    public CarResponseDTO update(Long id, CarRequestDTO request) {
        Car car = getCarOrThrow(id);
        CarMapper.updateEntity(car, request);
        return CarMapper.toResponse(carRepository.save(car));
    }

    @Transactional
    public void delete(Long id) {
        Car car = getCarOrThrow(id);
        carRepository.delete(car);
    }

    private Car getCarOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
    }
}
