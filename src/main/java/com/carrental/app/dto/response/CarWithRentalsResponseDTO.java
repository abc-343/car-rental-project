package com.carrental.app.dto.response;

import com.carrental.app.entity.CarStatus;

import java.math.BigDecimal;
import java.util.List;

// aggregated response: one car + all of its rentals
public record CarWithRentalsResponseDTO(
        Long id,
        String make,
        String model,
        int year,
        BigDecimal dailyRate,
        CarStatus status,
        List<RentalResponseDTO> rentals
) {
}
