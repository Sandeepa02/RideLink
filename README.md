# RideLink

RideLink is a microservices-based ride-sharing platform developed for the IT3130 Application Development group assignment. The project is designed to model a real-world transportation system using independent backend services that communicate with each other to handle accounts, driver management, fare estimation, and ride lifecycle operations.

## Overview

The platform is composed of four Spring Boot microservices:

- Account Service — handles user accounts and authentication
- Driver Service — manages driver profiles, assignment logic, and driver-related operations
- Fare & Payment Service — calculates fare estimates and manages payment-related logic
- Ride Service — orchestrates ride requests, assignment flow, and ride status transitions

Together, these services provide a full flow from passenger registration to ride creation, driver assignment, fare calculation, and status tracking.

## Architecture

```text
Client / Passenger
        |
        v
   account-service (8080)
        |
        v
   ride-service (8082)
      |        \
      |         \__ requests driver assignment from driver-service (8081)
      |
      \__ requests fare estimate from fare-payment-service (8083)
```

## Technology Stack

- Java 17
- Spring Boot
- Spring Web / MVC
- Spring Data MongoDB
- Spring Validation
- JWT-based authentication
- Maven
- MongoDB
- OpenAPI / Swagger documentation

## Repository Structure

```text
RideLink/
├── README.md
├── account-service/
├── driver-service/
├── fare-payment-service/
├── ride-service/
├── .gitignore
└── .github/
```

## Service Details

### 1. Account Service
- Runs on port 8080
- Handles user account data and authentication
- Uses JWT-based security for protected endpoints
- Stores account data in MongoDB

### 2. Driver Service
- Runs on port 8081
- Manages driver information and assignment-related logic
- Stores driver-related records in MongoDB
- Provides driver assignment support to the ride flow

### 3. Fare & Payment Service
- Runs on port 8083
- Calculates fares and supports payment/fare-related business logic
- Provides fare estimates to the ride service
- Uses MongoDB for fare/payment persistence

### 4. Ride Service
- Runs on port 8082
- Coordinates ride requests and ride lifecycle events
- Validates ride transitions and ride data
- Requests fare estimates and driver assignment from the other services
- Persists ride records in MongoDB

## Prerequisites

Before running the project locally, make sure you have:

- JDK 17 or later
- Maven
- MongoDB running locally or configured via a remote URI
- Git

## Environment Configuration

Each service relies on environment variables for MongoDB and JWT configuration.

Example required variables:

```bash
export MONGODB_URI="mongodb://localhost:27017"
export JWT_SECRET="your-secret-key"
export DRIVER_SERVICE_TOKEN="optional-token"
```

The configuration files for each service are located under:

```text
account-service/src/main/resources/application.properties
driver-service/src/main/resources/application.properties
fare-payment-service/src/main/resources/application.properties
ride-service/src/main/resources/application.properties
```

## Run Locally

### Account Service

```bash
cd account-service
./mvnw spring-boot:run
```

Windows:

```powershell
cd account-service
.\mvnw.cmd spring-boot:run
```

### Driver Service

```bash
cd driver-service
./mvnw spring-boot:run
```

### Fare & Payment Service

```bash
cd fare-payment-service
./mvnw spring-boot:run
```

### Ride Service

```bash
cd ride-service
./mvnw spring-boot:run
```

## Default Ports

```text
Account Service:  http://localhost:8080
Driver Service:  http://localhost:8081
Ride Service:     http://localhost:8082
Fare Service:     http://localhost:8083
```

## API Documentation

The ride service includes Swagger/OpenAPI support and exposes the UI at:

```text
http://localhost:8082/swagger-ui/index.html
```

This makes it easier to test and explore the ride-related endpoints during development.

## Typical Ride Flow

```text
1. Passenger creates a ride request
2. Ride Service requests a fare estimate from Fare & Payment Service
3. Ride Service requests a driver assignment from Driver Service
4. Ride is saved with status, fare, and driver information
5. Ride lifecycle updates are tracked until completion or cancellation
```

## Development Notes

This project is structured as a distributed backend system where each service owns a focused domain and communicates through HTTP-based integrations. This approach makes the system easier to scale, evolve, and maintain compared to a single monolithic application.

## Contributors

This project was developed as part of the IT3130 Application Development group assignment.

## License

This project does not currently specify a license file. If required, add a license before publishing or sharing the repository publicly.
