package com.carrental.app.presentationlayer.dto.response;

import com.carrental.app.dataaccesslayer.entity.CarStatus;

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
