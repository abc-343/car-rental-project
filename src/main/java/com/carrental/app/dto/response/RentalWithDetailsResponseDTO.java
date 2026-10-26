package com.carrental.app.dto.response;

import com.carrental.app.entity.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

// aggregated response: one rental + a summary of its customer and car
public record RentalWithDetailsResponseDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        RentalStatus status,
        CustomerSummaryDTO customer,
        CarSummaryDTO car
) {
}
