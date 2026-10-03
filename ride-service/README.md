# RideLink – Ride Management Service

The **Ride Management Service** is a backend microservice in the RideLink ride-sharing system. It manages ride requests, driver assignment, ride lifecycle transitions, and integration with the Fare & Payment Service.

## Responsibilities

The Ride Management Service is responsible for:

- Creating ride requests
- Storing pickup and destination locations
- Storing estimated distance and duration
- Requesting fare estimates from the Fare & Payment Service
- Storing the returned fare information
- Assigning available drivers through the Driver & Vehicle Service
- Managing the ride lifecycle
- Retrieving rides by ride ID
- Retrieving rides by passenger ID
- Retrieving rides by driver ID
- Validating ride status transitions
- Recording lifecycle timestamps
- Returning appropriate errors for invalid requests and missing rides

## Technology Stack

- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data MongoDB
- MongoDB
- Spring Validation
- SpringDoc OpenAPI / Swagger
- Maven
- JUnit 5
- Mockito

The project is compiled using Java release 17.

## Service Port

The Ride Management Service runs on:

```text
http://localhost:8082
```

## API Endpoints

### Create Ride

```http
POST /api/rides
```

Creates a new ride with `REQUESTED` status.

The Ride Service requests a fare estimate from the Fare & Payment Service before saving the ride.

Required request fields include:

- `passengerId`
- `pickupLocation`
- `destinationLocation`
- `distanceKm`
- `estimatedDurationMinutes`

Example:

```json
{
  "passengerId": "PASSENGER-001",
  "pickupLocation": {
    "address": "Colombo Fort",
    "latitude": 6.9344,
    "longitude": 79.8428
  },
  "destinationLocation": {
    "address": "Bambalapitiya",
    "latitude": 6.8910,
    "longitude": 79.8560
  },
  "distanceKm": 8.5,
  "estimatedDurationMinutes": 22
}
```

A valid Bearer JWT must be supplied in the `Authorization` header.

### Get Ride

```http
GET /api/rides/{id}
```

Returns a ride using its ride ID.

### Get Passenger Rides

```http
GET /api/rides/passenger/{passengerId}
```

Returns rides associated with a passenger.

### Get Driver Rides

```http
GET /api/rides/driver/{driverId}
```

Returns rides assigned to a driver.

### Update Ride Status

```http
PATCH /api/rides/{id}/status
```

Updates the lifecycle status of a ride.

Supported lifecycle states are:

```text
REQUESTED
ASSIGNED
ACCEPTED
IN_PROGRESS
COMPLETED
CANCELLED
```

Valid transitions are:

```text
REQUESTED  -> ASSIGNED
REQUESTED  -> CANCELLED

ASSIGNED   -> ACCEPTED
ASSIGNED   -> CANCELLED

ACCEPTED   -> IN_PROGRESS
ACCEPTED   -> CANCELLED

IN_PROGRESS -> COMPLETED
```

Completed and cancelled rides cannot transition to another status.

### Assign Driver

```http
POST /api/rides/{id}/assign
```

Requests driver assignment from the Driver & Vehicle Service for a requested ride.

When a driver is successfully assigned:

```text
REQUESTED -> ASSIGNED
```

The assigned driver ID is stored with the ride.

## Inter-Service Communication

The Ride Management Service communicates with two other RideLink microservices.

### Driver & Vehicle Service

The Ride Service calls:

```http
POST http://localhost:8081/api/drivers/assign
```

The request contains:

```json
{
  "rideId": "RIDE-001",
  "pickupLatitude": 6.9344,
  "pickupLongitude": 79.8428
}
```

The Driver & Vehicle Service selects an available driver and returns the assigned driver information.

Configuration:

```properties
driver.service.base-url=http://localhost:8081
driver.service.token=${DRIVER_SERVICE_TOKEN:}
```

The service token is supplied through environment/configuration rather than being hard-coded.

### Fare & Payment Service

The Ride Service requests a fare estimate using:

```http
POST http://localhost:8083/api/fares/estimate
```

The request contains:

```json
{
  "rideId": "RIDE-001",
  "distanceKm": 8.5,
  "estimatedDurationMinutes": 22
}
```

The Fare & Payment Service returns the generated fare information, including:

- `fareId`
- `rideId`
- `estimatedFare`

The Ride Service stores the returned `fareId` and `estimatedFare` with the ride.

The incoming Bearer JWT is forwarded to the Fare & Payment Service.

Configuration:

```properties
fare.service.base-url=http://localhost:8083
```

If the Fare Service request fails, the ride is not persisted. This prevents a ride from being created without a successful fare estimate.

## Ride Data

Each ride contains information including:

- Ride ID
- Passenger ID
- Driver ID
- Pickup location
- Destination location
- Ride status
- Distance
- Estimated duration
- Fare ID
- Estimated fare
- Final fare
- Requested timestamp
- Accepted timestamp
- Started timestamp
- Completed timestamp
- Cancelled timestamp

Ride IDs are generated using UUIDs.

## Validation

Request validation is implemented using Jakarta Bean Validation.

Examples include:

- Passenger ID must not be blank
- Pickup location is required
- Destination location is required
- Distance must be greater than zero
- Estimated duration must be greater than zero

Invalid ride lifecycle transitions are rejected by the service layer.

Missing rides are handled using a dedicated `RideNotFoundException`.

## Persistence

The Ride Management Service uses MongoDB for ride persistence.

MongoDB is configured as the service's own persistence boundary.

Ride documents are stored in the:

```text
rides
```

collection.

## Error Handling

The service provides centralized exception handling through:

```text
GlobalExceptionHandler
```

The service handles errors such as:

- Ride not found
- Invalid ride status transition
- Validation failures
- Invalid request data
- Inter-service communication failures

Fare estimation is performed before the ride is saved. Therefore, a failed Fare Service request does not result in a partially created ride.

## Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8082/swagger-ui/index.html
```

OpenAPI documentation is provided using SpringDoc.

The API documentation includes a Bearer JWT authentication scheme named:

```text
bearerAuth
```

Use the **Authorize** button in Swagger UI to provide the Bearer token when testing secured API interactions.

## Configuration

The main configuration is stored in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=ride-service
server.port=8082

driver.service.base-url=http://localhost:8081
driver.service.token=${DRIVER_SERVICE_TOKEN:}

fare.service.base-url=http://localhost:8083
```

Sensitive service credentials should be supplied through environment variables or external configuration rather than hard-coded in source code.

## Running the Service

From the `ride-service` directory:

```cmd
.\mvnw.cmd spring-boot:run
```

The service starts on:

```text
http://localhost:8082
```

MongoDB must be available for persistence.

The Driver & Vehicle Service and Fare & Payment Service should also be running when testing the complete inter-service workflow.

## Running Tests

Run the test suite using:

```cmd
.\mvnw.cmd test
```

The tests cover:

- Ride creation
- Ride retrieval
- Missing ride handling
- Valid ride status transitions
- Invalid ride status transitions
- Ride cancellation
- Lifecycle timestamp updates
- Fare Service integration
- Fare Service failure handling
- Prevention of ride persistence when fare estimation fails

## Test Result

The current test suite contains unit and Spring context tests covering the Ride Management Service functionality.

Example successful test result:

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

## Project Structure

```text
src/main/java/com/ridelink/ride
├── client
│   ├── DriverServiceClient.java
│   └── FareServiceClient.java
├── config
│   └── OpenApiConfig.java
├── controller
│   └── RideController.java
├── dto
│   ├── CreateRideRequest.java
│   ├── DriverAssignmentRequest.java
│   ├── DriverAssignmentResponse.java
│   ├── FareEstimateRequest.java
│   ├── FareEstimateResponse.java
│   ├── LocationRequest.java
│   ├── RideResponse.java
│   └── UpdateRideStatusRequest.java
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── InvalidRideStatusException.java
│   └── RideNotFoundException.java
├── model
│   ├── Location.java
│   ├── Ride.java
│   └── RideStatus.java
├── repository
│   └── RideRepository.java
├── service
│   └── RideService.java
└── RideServiceApplication.java
```

## Inter-Service Ride Creation Flow

The normal ride creation flow is:

```text
Passenger / Client
       |
       | POST /api/rides
       v
Ride Management Service
       |
       | Request fare estimate
       v
Fare & Payment Service
       |
       | fareId + estimatedFare
       v
Ride Management Service
       |
       | Save ride
       v
MongoDB
```

Driver assignment is handled separately:

```text
Ride Management Service
       |
       | POST /api/drivers/assign
       v
Driver & Vehicle Service
       |
       | Assigned driver
       v
Ride Management Service
       |
       | Store driverId
       v
MongoDB
```

## Git Contribution

Development of this service is maintained on the Ride Service feature branch:

```text
Feature/ride-service
```

Changes are committed using meaningful feature, test, configuration, and documentation commit messages.