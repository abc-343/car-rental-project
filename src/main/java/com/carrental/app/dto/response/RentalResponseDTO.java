package com.carrental.app.dto.response;

import com.carrental.app.entity.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentalResponseDTO(
        Long id,
        Long customerId,
        Long carId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        RentalStatus status
) {
}
