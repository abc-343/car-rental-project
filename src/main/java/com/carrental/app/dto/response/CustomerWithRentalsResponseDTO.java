package com.carrental.app.dto.response;

import java.util.List;

// aggregated response: one customer + all of their rentals
public record CustomerWithRentalsResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        List<RentalResponseDTO> rentals
) {
}
