package com.carrental.app.customermanagement.presentationlayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    // public id (e.g. CAR-0001), the database id is never exposed
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
