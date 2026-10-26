package com.carrental.app.mapper;

import com.carrental.app.dto.request.CarRequestDTO;
import com.carrental.app.dto.response.CarResponseDTO;
import com.carrental.app.dto.response.CarSummaryDTO;
import com.carrental.app.dto.response.CarWithRentalsResponseDTO;
import com.carrental.app.dto.response.RentalResponseDTO;
import com.carrental.app.entity.Car;

import java.util.List;

public class CarMapper {

    public static Car toEntity(CarRequestDTO dto) {
        return new Car(dto.make(), dto.model(), dto.year(), dto.dailyRate(), dto.status());
    }

    // copies the request values onto an existing car (used by PUT)
    public static void updateEntity(Car car, CarRequestDTO dto) {
        car.setMake(dto.make());
        car.setModel(dto.model());
        car.setYear(dto.year());
        car.setDailyRate(dto.dailyRate());
        car.setStatus(dto.status());
    }

    public static CarResponseDTO toResponse(Car car) {
        return new CarResponseDTO(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getDailyRate(),
                car.getStatus()
        );
    }

    public static CarSummaryDTO toSummary(Car car) {
        return new CarSummaryDTO(car.getId(), car.getMake(), car.getModel(), car.getYear());
    }

    public static CarWithRentalsResponseDTO toWithRentals(Car car) {
        List<RentalResponseDTO> rentals = car.getRentals().stream()
                .map(RentalMapper::toResponse)
                .toList();

        return new CarWithRentalsResponseDTO(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getDailyRate(),
                car.getStatus(),
                rentals
        );
    }
}
