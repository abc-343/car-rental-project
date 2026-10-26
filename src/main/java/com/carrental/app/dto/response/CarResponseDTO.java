package com.carrental.app.dto.response;

import com.carrental.app.entity.CarStatus;

import java.math.BigDecimal;

public record CarResponseDTO(
        Long id,
        String make,
        String model,
        int year,
        BigDecimal dailyRate,
        CarStatus status
) {
}
