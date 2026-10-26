package com.carrental.app.dto.request;

import com.carrental.app.entity.CarStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CarRequestDTO(
        @NotBlank(message = "make is required")
        @Size(max = 50, message = "make must be at most 50 characters")
        String make,

        @NotBlank(message = "model is required")
        @Size(max = 50, message = "model must be at most 50 characters")
        String model,

        @Min(value = 1886, message = "year must be 1886 or later")
        @Max(value = 2100, message = "year must be 2100 or earlier")
        int year,

        @NotNull(message = "dailyRate is required")
        @Positive(message = "dailyRate must be greater than 0")
        BigDecimal dailyRate,

        @NotNull(message = "status is required")
        CarStatus status
) {
}
