package com.carrental.app.carmanagement.presentationlayer.dto.request;

import com.carrental.app.carmanagement.domainlayer.CarStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// id is not here because the server generates it
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {

    @NotBlank(message = "make is required")
    @Size(max = 50, message = "make must be at most 50 characters")
    private String make;

    @NotBlank(message = "model is required")
    @Size(max = 50, message = "model must be at most 50 characters")
    private String model;

    @Min(value = 1886, message = "year must be 1886 or later")
    @Max(value = 2100, message = "year must be 2100 or earlier")
    private int year;

    @NotNull(message = "dailyRate is required")
    @Positive(message = "dailyRate must be greater than 0")
    private BigDecimal dailyRate;

    @NotNull(message = "status is required")
    private CarStatus status;
}
