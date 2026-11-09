package com.carrental.app.rentalmanagement.domainlayer;

import com.carrental.app.carmanagement.domainlayer.Car;
import com.carrental.app.customermanagement.domainlayer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// Lombok generates the getters, setters and the no-argument constructor JPA needs.
// The warning suppressed below comes from IntelliJ having no database connection to check the
// table/column names against (H2 only exists while the app runs); the tables are created from these annotations.
@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // internal database id, never sent to the client

    // public id used in the API and in URLs, e.g. RNT-0001
    @Column(name = "rental_id", nullable = false, unique = true, length = 20)
    private String rentalId;

    // many rentals -> one customer (FK customer_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // many rentals -> one car (FK car_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;
}
