package com.carrental.app.rentalmanagement.presentationlayer.controller;

import com.carrental.app.rentalmanagement.businesslayer.RentalService;
import com.carrental.app.rentalmanagement.domainlayer.Rental;
import com.carrental.app.rentalmanagement.domainlayer.RentalStatus;
import com.carrental.app.rentalmanagement.presentationlayer.dto.request.RentalRequestDTO;
import com.carrental.app.rentalmanagement.presentationlayer.dto.response.RentalResponseDTO;
import com.carrental.app.rentalmanagement.presentationlayer.dto.response.RentalWithDetailsResponseDTO;
import com.carrental.app.rentalmanagement.datamappinglayer.RentalMapper;
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
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    // GET /api/rentals
    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        return ResponseEntity.ok(rentalMapper.entityListToResponseModelList(rentalService.getAllRentals()));
    }

    // GET /api/rentals?status=ACTIVE   (RESERVED, ACTIVE, COMPLETED or CANCELLED)
    @GetMapping(params = "status")
    public ResponseEntity<List<RentalResponseDTO>> getRentalsByStatus(@RequestParam RentalStatus status) {
        return ResponseEntity.ok(rentalMapper.entityListToResponseModelList(rentalService.getRentalsByStatus(status)));
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable String rentalId) {
        return ResponseEntity.ok(rentalMapper.entityToResponseModel(rentalService.getRentalByRentalId(rentalId)));
    }

    // aggregated: rental + customer summary + car summary
    @GetMapping("/{rentalId}/details")
    public ResponseEntity<RentalWithDetailsResponseDTO> getRentalWithDetails(@PathVariable String rentalId) {
        return ResponseEntity.ok(rentalMapper.entityToRentalWithDetailsResponseModel(rentalService.getRentalByRentalId(rentalId)));
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@Valid @RequestBody RentalRequestDTO request) {
        Rental savedRental = rentalService.addRental(rentalMapper.requestModelToEntity(request));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{rentalId}")
                .buildAndExpand(savedRental.getRentalId())
                .toUri();

        return ResponseEntity.created(location).body(rentalMapper.entityToResponseModel(savedRental));
    }

    @PutMapping("/{rentalId}")
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable String rentalId,
                                                          @Valid @RequestBody RentalRequestDTO request) {
        Rental updatedRental = rentalService.updateRental(rentalId, rentalMapper.requestModelToEntity(request));
        return ResponseEntity.ok(rentalMapper.entityToResponseModel(updatedRental));
    }

    @DeleteMapping("/{rentalId}")
    public ResponseEntity<Void> deleteRental(@PathVariable String rentalId) {
        rentalService.deleteByRentalId(rentalId);
        return ResponseEntity.noContent().build();
    }
}
