package com.carrental.app.rentalmanagement.presentationlayer.dto.response;

import com.carrental.app.carmanagement.presentationlayer.dto.response.CarSummaryDTO;
import com.carrental.app.customermanagement.presentationlayer.dto.response.CustomerSummaryDTO;
import com.carrental.app.rentalmanagement.domainlayer.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// aggregated response: one rental + a summary of its customer and car
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalWithDetailsResponseDTO {

    private String rentalId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private RentalStatus status;
    private CustomerSummaryDTO customer;
    private CarSummaryDTO car;
}
