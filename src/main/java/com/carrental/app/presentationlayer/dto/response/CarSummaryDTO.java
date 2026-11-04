package com.carrental.app.presentationlayer.dto.response;

// small version of a car, used inside other responses
public record CarSummaryDTO(
        Long id,
        String make,
        String model,
        int year
) {
}
