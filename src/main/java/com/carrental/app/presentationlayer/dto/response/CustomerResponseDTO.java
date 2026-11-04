package com.carrental.app.presentationlayer.dto.response;

public record CustomerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
