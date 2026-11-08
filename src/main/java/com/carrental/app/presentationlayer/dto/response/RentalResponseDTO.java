package com.carrental.app.presentationlayer.dto.response;

import com.carrental.app.domainlayer.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDTO {

    // public UUID, the database id is never exposed
    private String rentalId;
    private String customerId;
    private String carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private RentalStatus status;
}
