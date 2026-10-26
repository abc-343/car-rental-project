package com.carrental.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(
        @NotBlank(message = "firstName is required")
        @Size(max = 50, message = "firstName must be at most 50 characters")
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(max = 50, message = "lastName must be at most 50 characters")
        String lastName,

        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @NotBlank(message = "phone is required")
        @Size(max = 20, message = "phone must be at most 20 characters")
        String phone
) {
}
