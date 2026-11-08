# Car Rental API (Backend)

Spring Boot REST API for the Car Rental System designed in Part 1.
Resources: **Car**, **Customer**, **Rental**.

**Public base URL:** `https://car-rental-project-k0e4.onrender.com/`

- Java 17, Spring Boot 3.5, Gradle
- Spring Web, Spring Data JPA, Bean Validation
- H2 in-memory database (seeded from `data.sql` on startup)
- Lombok for the request/response models
- springdoc-openapi for Swagger UI

## Run locally (IntelliJ)

1. File → Open → select the `car-rental-api` folder (IntelliJ detects the Gradle project).
2. Wait for Gradle to sync.
3. Run `CarRentalApplication` (green arrow next to `main`).
4. The API runs on `http://localhost:8080`.

Or from a terminal: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`).

### Swagger UI (API documentation)

- `http://localhost:8080/swagger-ui.html` (live: `https://car-rental-project-k0e4.onrender.com/swagger-ui.html`)
- Raw OpenAPI JSON: `/v3/api-docs`

Every endpoint can be tried from the browser there.

### H2 console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:carrentaldb`
- User: `sa`, password: (empty)

## Project structure

```
com.carrental.app
├── config
├── exception
├── presentationlayer
│   ├── controller
│   └── dto
│       ├── request
│       └── response
├── datamappinglayer
├── businesslayer
├── domainlayer
└── dataaccesslayer
resources/
├── application.properties
└── data.sql
```

## Relationships

- Customer (1) → Rental (many) — FK `rentals.customer_id`
- Car (1) → Rental (many) — FK `rentals.car_id`
- Rental is the join entity between Customer and Car.

Deleting a car or a customer also deletes their rentals (cascade).

## Endpoints

| Method | Endpoint                            | Description                                             | Success        |
| ------ | ----------------------------------- | ------------------------------------------------------- | -------------- |
| GET    | /api/cars                           | All cars (optional filter: `?status=AVAILABLE`)         | 200            |
| GET    | /api/cars/{carId}                   | One car                                                 | 200            |
| GET    | /api/cars/{carId}/rentals           | **Aggregated:** car + its rentals                       | 200            |
| POST   | /api/cars                           | Create car                                              | 201 + Location |
| PUT    | /api/cars/{carId}                   | Update car                                              | 200            |
| DELETE | /api/cars/{carId}                   | Delete car                                              | 204            |
| GET    | /api/customers                      | All customers                                           | 200            |
| GET    | /api/customers/{customerId}         | One customer                                            | 200            |
| GET    | /api/customers/{customerId}/rentals | **Aggregated:** customer + their rentals                | 200            |
| POST   | /api/customers                      | Create customer                                         | 201 + Location |
| PUT    | /api/customers/{customerId}         | Update customer                                         | 200            |
| DELETE | /api/customers/{customerId}         | Delete customer                                         | 204            |
| GET    | /api/rentals                        | All rentals (optional filter: `?status=ACTIVE`)         | 200            |
| GET    | /api/rentals/{rentalId}             | One rental                                              | 200            |
| GET    | /api/rentals/{rentalId}/details     | **Aggregated:** rental + customer summary + car summary | 200            |
| POST   | /api/rentals                        | Create rental (totalCost is calculated)                 | 201 + Location |
| PUT    | /api/rentals/{rentalId}             | Update rental                                           | 200            |
| DELETE | /api/rentals/{rentalId}             | Delete rental                                           | 204            |

### Request flow

`Controller` → mapper `requestModelToEntity` → `Service` (works with entities, validates, applies the
business rules, calls the `Repository`) → back in the controller, mapper `entityToResponseModel` /
`entityListToResponseModelList` builds the response DTO.

### Business rules (service implementations)

- `totalCost = number of days × car dailyRate` (a same-day rental counts as 1 day), always calculated by the server
- `endDate` cannot be before `startDate` → 400
- A car in `MAINTENANCE` cannot be RESERVED/ACTIVE → 400
- A car cannot be booked twice for overlapping dates (RESERVED/ACTIVE rentals) → 409
- A car that is on an ACTIVE rental cannot be put into `MAINTENANCE` → 409
- Customer email must be unique (case-insensitive) → 409
- Updating a car or customer keeps its existing rentals

### Error format

```json
{
  "timestamp": "2026-10-25T21:13:21.003",
  "status": 404,
  "error": "Not Found",
  "message": "Rental RNT-9999 not found",
  "path": "/api/rentals/RNT-9999"
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
  -d '{"customerId":"CUS-0001","carId":"CAR-0005","startDate":"2026-12-01","endDate":"2026-12-05","status":"RESERVED"}'

# aggregated
curl http://localhost:8080/api/customers/CUS-0001/rentals

# filter
curl "http://localhost:8080/api/cars?status=AVAILABLE"

# delete -> 204
curl -i -X DELETE http://localhost:8080/api/rentals/RNT-0012

# missing id -> 404
curl -i http://localhost:8080/api/cars/CAR-9999
```

## Postman

Import one of the collections from the `postman/` folder (Postman → Import):

| File                                                 | Targets                                         |
| ---------------------------------------------------- | ----------------------------------------------- |
| `postman/car-rental-api.postman_collection.json`     | Local app (`http://localhost:8080`)             |
| `postman/car-rental-api-render.postman_collection.json` | Live deployment (`https://car-rental-project-k0e4.onrender.com`) |

Both contain the same requests (CRUD for cars, customers and rentals, the aggregated endpoints, and 400/404/409 error cases), each with status-code tests.
The base URL is the collection variable `baseUrl`.

Note: the free Render instance sleeps when idle, so the first request can take about a minute. The H2 database resets to the seed data on every restart.
