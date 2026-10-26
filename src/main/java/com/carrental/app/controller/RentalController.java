package com.carrental.app.controller;

import com.carrental.app.dto.request.RentalRequestDTO;
import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.dto.response.RentalWithDetailsResponseDTO;
import com.carrental.app.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findById(id));
    }

    // aggregated: rental + customer summary + car summary
    @GetMapping("/{id}/details")
    public ResponseEntity<RentalWithDetailsResponseDTO> getRentalWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findRentalWithDetails(id));
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@Valid @RequestBody RentalRequestDTO request) {
        RentalResponseDTO created = rentalService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable Long id,
                                                          @Valid @RequestBody RentalRequestDTO request) {
        return ResponseEntity.ok(rentalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
