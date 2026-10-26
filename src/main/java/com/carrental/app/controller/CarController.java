package com.carrental.app.controller;

import com.carrental.app.dto.request.CarRequestDTO;
import com.carrental.app.dto.response.CarResponseDTO;
import com.carrental.app.dto.response.CarWithRentalsResponseDTO;
import com.carrental.app.service.CarService;
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

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    // aggregated: car + all its rentals
    @GetMapping("/{id}/rentals")
    public ResponseEntity<CarWithRentalsResponseDTO> getCarWithRentals(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findCarWithRentals(id));
    }

    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(@Valid @RequestBody CarRequestDTO request) {
        CarResponseDTO created = carService.create(request);

        // Location: /api/cars/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Long id,
                                                    @Valid @RequestBody CarRequestDTO request) {
        return ResponseEntity.ok(carService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
