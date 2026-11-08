package com.carrental.app.datamapping;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.presentationlayer.dto.request.CarRequestDTO;
import com.carrental.app.presentationlayer.dto.response.CarResponseDTO;
import com.carrental.app.presentationlayer.dto.response.CarWithRentalsResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarMapper {

    private final RentalMapper rentalMapper;

    public CarMapper(RentalMapper rentalMapper) {
        this.rentalMapper = rentalMapper;
    }

    public CarResponseDTO entityToResponseModel(Car car) {
        return new CarResponseDTO(
                car.getCarId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getDailyRate(),
                car.getStatus()
        );
    }

    public List<CarResponseDTO> entityListToResponseModelList(List<Car> cars) {
        return cars.stream()
                .map(this::entityToResponseModel)
                .toList();
    }

    // id / public id are not copied: the request never contains them (the server generates them)
    public Car requestModelToEntity(CarRequestDTO request) {
        return new Car(request.getMake(), request.getModel(), request.getYear(), request.getDailyRate(), request.getStatus());
    }

    // aggregated: car + all of its rentals
    public CarWithRentalsResponseDTO entityToCarWithRentalsResponseModel(Car car) {
        return new CarWithRentalsResponseDTO(
                car.getCarId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getDailyRate(),
                car.getStatus(),
                rentalMapper.entityListToResponseModelList(car.getRentals())
        );
    }
}
