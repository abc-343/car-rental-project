package com.carrental.app.presentationlayer.dto.response;

import com.carrental.app.domainlayer.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {

    // public id (e.g. CAR-0001), the database id is never exposed
    private String carId;
    private String make;
    private String model;
    private int year;
    private BigDecimal dailyRate;
    private CarStatus status;
}
