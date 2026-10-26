package com.carrental.app.dto.request;

import com.carrental.app.entity.RentalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

// totalCost is not here because the server calculates it
public record RentalRequestDTO(
        @NotNull(message = "customerId is required")
        @Positive(message = "customerId must be positive")
        Long customerId,

        @NotNull(message = "carId is required")
        @Positive(message = "carId must be positive")
        Long carId,

        @NotNull(message = "startDate is required")
        LocalDate startDate,

        @NotNull(message = "endDate is required")
        LocalDate endDate,

        @NotNull(message = "status is required")
        RentalStatus status
) {
}
