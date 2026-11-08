package com.carrental.app.presentationlayer.controller;

import com.carrental.app.businesslayer.CarService;
import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.CarStatus;
import com.carrental.app.presentationlayer.dto.request.CarRequestDTO;
import com.carrental.app.presentationlayer.dto.response.CarResponseDTO;
import com.carrental.app.presentationlayer.dto.response.CarWithRentalsResponseDTO;
import com.carrental.app.datamappinglayer.CarMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;
    private final CarMapper carMapper;

    public CarController(CarService carService, CarMapper carMapper) {
        this.carService = carService;
        this.carMapper = carMapper;
    }

    // GET /api/cars
    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars() {
        return ResponseEntity.ok(carMapper.entityListToResponseModelList(carService.getAllCars()));
    }

    // GET /api/cars?status=AVAILABLE   (AVAILABLE, RENTED or MAINTENANCE)
    @GetMapping(params = "status")
    public ResponseEntity<List<CarResponseDTO>> getCarsByStatus(@RequestParam CarStatus status) {
        return ResponseEntity.ok(carMapper.entityListToResponseModelList(carService.getCarsByStatus(status)));
    }

    @GetMapping("/{carId}")
    public ResponseEntity<CarResponseDTO> getCarById(@PathVariable String carId) {
        return ResponseEntity.ok(carMapper.entityToResponseModel(carService.getCarByCarId(carId)));
    }

    // aggregated: car + all its rentals
    @GetMapping("/{carId}/rentals")
    public ResponseEntity<CarWithRentalsResponseDTO> getCarWithRentals(@PathVariable String carId) {
        return ResponseEntity.ok(carMapper.entityToCarWithRentalsResponseModel(carService.getCarByCarId(carId)));
    }

    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(@Valid @RequestBody CarRequestDTO request) {
        Car savedCar = carService.addCar(carMapper.requestModelToEntity(request));

        // Location: /api/cars/{carId}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{carId}")
                .buildAndExpand(savedCar.getCarId())
                .toUri();

        return ResponseEntity.created(location).body(carMapper.entityToResponseModel(savedCar));
    }

    // PUT replaces every field, so calling it twice gives the same result (idempotent)
    @PutMapping("/{carId}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable String carId,
                                                    @Valid @RequestBody CarRequestDTO request) {
        Car updatedCar = carService.updateCar(carId, carMapper.requestModelToEntity(request));
        return ResponseEntity.ok(carMapper.entityToResponseModel(updatedCar));
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
        carService.deleteByCarId(carId);
        return ResponseEntity.noContent().build();
    }
}
