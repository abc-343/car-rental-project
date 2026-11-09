package com.carrental.app.carmanagement.presentationlayer.dto.response;

import com.carrental.app.carmanagement.domainlayer.CarStatus;
import com.carrental.app.rentalmanagement.presentationlayer.dto.response.RentalResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// aggregated response: one car + all of its rentals
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWithRentalsResponseDTO {

    private String carId;
    private String make;
    private String model;
    private int year;
    private BigDecimal dailyRate;
    private CarStatus status;
    private List<RentalResponseDTO> rentals;
}
