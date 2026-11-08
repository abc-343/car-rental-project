package com.carrental.app.presentationlayer.dto.request;

import com.carrental.app.domainlayer.RentalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// id and totalCost are not here because the server generates/calculates them
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDTO {

    // public ids (UUIDs) of the customer and the car
    @NotBlank(message = "customerId is required")
    private String customerId;

    @NotBlank(message = "carId is required")
    private String carId;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @NotNull(message = "endDate is required")
    private LocalDate endDate;

    @NotNull(message = "status is required")
    private RentalStatus status;
}
