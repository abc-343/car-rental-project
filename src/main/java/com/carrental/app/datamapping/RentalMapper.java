package com.carrental.app.datamapping;

import com.carrental.app.domainlayer.Car;
import com.carrental.app.domainlayer.Customer;
import com.carrental.app.domainlayer.Rental;
import com.carrental.app.presentationlayer.dto.request.RentalRequestDTO;
import com.carrental.app.presentationlayer.dto.response.CarSummaryDTO;
import com.carrental.app.presentationlayer.dto.response.CustomerSummaryDTO;
import com.carrental.app.presentationlayer.dto.response.RentalResponseDTO;
import com.carrental.app.presentationlayer.dto.response.RentalWithDetailsResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RentalMapper {

    public RentalResponseDTO entityToResponseModel(Rental rental) {
        return new RentalResponseDTO(
                rental.getRentalId(),
                rental.getCustomer().getCustomerId(),
                rental.getCar().getCarId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalCost(),
                rental.getStatus()
        );
    }

    public List<RentalResponseDTO> entityListToResponseModelList(List<Rental> rentals) {
        return rentals.stream()
                .map(this::entityToResponseModel)
                .toList();
    }

    // The request only has the customer and car public ids, so the rental gets "placeholder"
    // Customer/Car objects holding just that id. RentalServiceImpl replaces them with the
    // real ones from the database (and throws 404 if they don't exist).
    // rentalId and totalCost are not set here because the service generates/calculates them.
    public Rental requestModelToEntity(RentalRequestDTO request) {
        Customer customer = new Customer();
        customer.setCustomerId(request.getCustomerId());

        Car car = new Car();
        car.setCarId(request.getCarId());

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setCar(car);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setStatus(request.getStatus());
        return rental;
    }

    // aggregated: rental + a summary of its customer and its car
    public RentalWithDetailsResponseDTO entityToRentalWithDetailsResponseModel(Rental rental) {
        Customer customer = rental.getCustomer();
        Car car = rental.getCar();

        CustomerSummaryDTO customerSummary = new CustomerSummaryDTO(
                customer.getCustomerId(),
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail()
        );
        CarSummaryDTO carSummary = new CarSummaryDTO(car.getCarId(), car.getMake(), car.getModel(), car.getYear());

        return new RentalWithDetailsResponseDTO(
                rental.getRentalId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalCost(),
                rental.getStatus(),
                customerSummary,
                carSummary
        );
    }
}
