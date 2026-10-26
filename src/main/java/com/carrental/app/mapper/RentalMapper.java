package com.carrental.app.mapper;

import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.dto.response.RentalWithDetailsResponseDTO;
import com.carrental.app.entity.Rental;

// no toEntity() here because a rental needs the Customer and Car
// loaded from the database first, so RentalService builds it
public class RentalMapper {

    public static RentalResponseDTO toResponse(Rental rental) {
        return new RentalResponseDTO(
                rental.getId(),
                rental.getCustomer().getId(),
                rental.getCar().getId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalCost(),
                rental.getStatus()
        );
    }

    public static RentalWithDetailsResponseDTO toWithDetails(Rental rental) {
        return new RentalWithDetailsResponseDTO(
                rental.getId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalCost(),
                rental.getStatus(),
                CustomerMapper.toSummary(rental.getCustomer()),
                CarMapper.toSummary(rental.getCar())
        );
    }
}
