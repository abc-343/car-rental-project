package com.carrental.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger UI: http://localhost:8080/swagger-ui.html
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carRentalOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Rental API")
                        .version("1.0.0")
                        .description("REST API for the Car Rental System: cars, customers and rentals. "
                                + "Resources are identified by public UUIDs (carId, customerId, rentalId)."));
    }
}
