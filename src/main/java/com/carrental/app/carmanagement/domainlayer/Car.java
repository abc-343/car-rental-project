package com.carrental.app.carmanagement.domainlayer;

import com.carrental.app.rentalmanagement.domainlayer.Rental;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Lombok generates the getters, setters and the no-argument constructor JPA needs.
// The warning suppressed below comes from IntelliJ having no database connection to check the
// table/column names against (H2 only exists while the app runs); the tables are created from these annotations.
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // internal database id, never sent to the client

    // public id used in the API and in URLs, e.g. CAR-0001
    @Column(name = "car_id", nullable = false, unique = true, length = 20)
    private String carId;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    // "year" is a reserved word in H2 so the column is named car_year
    @Column(name = "car_year", nullable = false)
    private int year;

    @Column(name = "daily_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    // one car -> many rentals. If a car is deleted its rentals are deleted too
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();

    public Car(String make, String model, int year, BigDecimal dailyRate, CarStatus status) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.status = status;
    }
}
