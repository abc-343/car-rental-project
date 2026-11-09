package com.carrental.app.customermanagement.presentationlayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// small version of a customer, used inside other responses
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDTO {

    private String customerId;
    private String fullName;
    private String email;
}
