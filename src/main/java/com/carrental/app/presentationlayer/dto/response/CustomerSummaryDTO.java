package com.carrental.app.presentationlayer.dto.response;

// small version of a customer, used inside other responses
public record CustomerSummaryDTO(
        Long id,
        String fullName,
        String email
) {
}
