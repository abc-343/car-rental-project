package com.carrental.app.carmanagement.presentationlayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// small version of a car, used inside other responses
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSummaryDTO {

    private String carId;
    private String make;
    private String model;
    private int year;
}
