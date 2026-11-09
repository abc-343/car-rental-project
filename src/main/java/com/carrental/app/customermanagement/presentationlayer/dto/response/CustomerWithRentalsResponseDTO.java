package com.carrental.app.customermanagement.presentationlayer.dto.response;

import com.carrental.app.rentalmanagement.presentationlayer.dto.response.RentalResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// aggregated response: one customer + all of their rentals
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWithRentalsResponseDTO {

    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<RentalResponseDTO> rentals;
}
