# Car Rental API — Part 2 (Backend)

Spring Boot REST API for the Car Rental System designed in Part 1.
Resources: **Car**, **Customer**, **Rental**.

**Public base URL:** `https://YOUR-APP-NAME.onrender.com`

## Tech

- Java 17, Spring Boot 3.5, Gradle
- Spring Web, Spring Data JPA, Bean Validation
- H2 in-memory database (seeded from `data.sql` on startup)
- JUnit 5, Mockito, MockMvc for tests

## Run locally (IntelliJ)

1. File → Open → select the `car-rental-api` folder (IntelliJ detects the Gradle project).
2. Wait for Gradle to sync.
3. Run `CarRentalApplication` (green arrow next to `main`).
4. The API runs on `http://localhost:8080`.

Or from a terminal: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`).

### H2 console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:carrentaldb`
- User: `sa`, password: (empty)

## Project structure

```
com.carrental.app
├── config        WebConfig (CORS for the React frontend)
├── controller    REST controllers (presentation layer)
├── dto
│   ├── request   CarRequestDTO, CustomerRequestDTO, RentalRequestDTO
│   └── response  ResponseDTOs, SummaryDTOs and aggregated DTOs
├── entity        JPA entities (domain layer) + enums
├── exception     NotFoundException(s), GlobalExceptionHandler, ErrorResponse
├── mapper        Manual entity <-> DTO mappers
├── repository    Spring Data JPA repositories (data access layer)
└── service       Business logic layer
resources/
├── application.properties
└── data.sql      Seed data (12 rows per table)
```

## Relationships

- Customer (1) → Rental (many) — FK `rentals.customer_id`
- Car (1) → Rental (many) — FK `rentals.car_id`
- Rental is the join entity between Customer and Car.

Deleting a car or a customer also deletes their rentals (cascade).

## Endpoints

| Method | Endpoint                    | Description                                             | Success        |
| ------ | --------------------------- | ------------------------------------------------------- | -------------- |
| GET    | /api/cars                   | All cars                                                | 200            |
| GET    | /api/cars/{id}              | One car                                                 | 200            |
| GET    | /api/cars/{id}/rentals      | **Aggregated:** car + its rentals                       | 200            |
| POST   | /api/cars                   | Create car                                              | 201 + Location |
| PUT    | /api/cars/{id}              | Update car                                              | 200            |
| DELETE | /api/cars/{id}              | Delete car                                              | 204            |
| GET    | /api/customers              | All customers                                           | 200            |
| GET    | /api/customers/{id}         | One customer                                            | 200            |
| GET    | /api/customers/{id}/rentals | **Aggregated:** customer + their rentals                | 200            |
| POST   | /api/customers              | Create customer                                         | 201 + Location |
| PUT    | /api/customers/{id}         | Update customer                                         | 200            |
| DELETE | /api/customers/{id}         | Delete customer                                         | 204            |
| GET    | /api/rentals                | All rentals                                             | 200            |
| GET    | /api/rentals/{id}           | One rental                                              | 200            |
| GET    | /api/rentals/{id}/details   | **Aggregated:** rental + customer summary + car summary | 200            |
| POST   | /api/rentals                | Create rental (totalCost is calculated)                 | 201 + Location |
| PUT    | /api/rentals/{id}           | Update rental                                           | 200            |
| DELETE | /api/rentals/{id}           | Delete rental                                           | 204            |

### Business rules (RentalService)

- `totalCost = number of days × car dailyRate` (a same-day rental counts as 1 day)
- `endDate` cannot be before `startDate` → 400
- A car in `MAINTENANCE` cannot be rented → 400
- Customer email must be unique → 409

### Error format

```json
{
  "timestamp": "2026-10-25T21:13:21.003",
  "status": 404,
  "error": "Not Found",
  "message": "Rental 123 not found",
  "path": "/api/rentals/123"
}
```

Validation errors (400) also include a `fieldErrors` object, e.g.
`"fieldErrors": { "email": "email must be a valid email address" }`.

## Example requests (curl)

```bash
# create a customer -> 201
curl -i -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane.doe@email.com","phone":"514-555-0199"}'

# create a rental -> 201, totalCost = 4 days * 52.00 = 208.00
curl -i -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"carId":5,"startDate":"2026-12-01","endDate":"2026-12-05","status":"RESERVED"}'

# aggregated
curl http://localhost:8080/api/customers/1/rentals

# delete -> 204
curl -i -X DELETE http://localhost:8080/api/rentals/12

# missing id -> 404
curl -i http://localhost:8080/api/cars/9999
```
