# RideLink - Backend Microservices Platform

Backend microservices solution for a ride-sharing platform, developed for the **IT3130 – Application Development** group assignment.

---

## 1. Team Members & Service Ownership

| Member Name | Student ID | Primary Microservice | Responsibilities |
| :--- | :--- | :--- | :--- |
| **Harindu Wimalasinghe** | *(Student ID)* | Account Service | User registration, authentication, role management, profile viewing and updates |
| **Suraj Sandeepa** | *(Student ID)* | Driver & Vehicle Service | Driver profiles, vehicle registration, availability status, simulated location, driver matching |
| **Amadi Hettiarachchi** | *(Student ID)* | Ride Management Service | Ride requests, lifecycle state transitions, driver assignment, fare estimation integration |
| **Dharmi Atulugama** | *(Student ID)* | Fare & Payment Service | Fare estimation rule, final fare calculation, simulated payment processing, receipts |

---

## 2. System Architecture & Service Registry

The application consists of four independent Spring Boot services, each running on its own dedicated port and maintaining an isolated database boundary on MongoDB Atlas:

| Service | Port | Database Name | Swagger UI URL |
| :--- | :--- | :--- | :--- |
| **Account Service** | `8080` | `ridelink_account` | http://localhost:8080/swagger-ui.html |
| **Driver & Vehicle Service** | `8081` | `ridelink_driver` | http://localhost:8081/swagger-ui.html |
| **Ride Management Service** | `8082` | `ridelink_ride` | http://localhost:8082/swagger-ui.html |
| **Fare & Payment Service** | `8083` | `ridelink_fare` | http://localhost:8083/swagger-ui.html |

### Interservice Communication
* **Ride Service $\rightarrow$ Fare & Payment Service:** Synchronous REST call (`POST /api/fares/estimate`) to calculate estimated fares when a new ride is created.
* **Ride Service $\rightarrow$ Driver Service:** Synchronous REST call (`POST /api/drivers/assign`) to locate and allocate the nearest available driver when ride assignment is requested.

---

## 3. Technology Stack

* **Language:** Java 17
* **Framework:** Spring Boot 4.1.x (Spring Web, Spring Data MongoDB, Spring Validation, Spring Security)
* **Database:** MongoDB Atlas (separate database per service)
* **Authentication:** JWT (HMAC-SHA256) with role-based access control (`ROLE_PASSENGER`, `ROLE_DRIVER`, `ROLE_ADMIN`)
* **API Documentation:** SpringDoc OpenAPI 3 / Swagger UI
* **Build Tool:** Maven (with Maven Wrapper `mvnw` included in all services)
* **CI/CD:** GitHub Actions (independent workflows per service)
* **Testing:** JUnit 5, Mockito, Postman Collection

---

## 4. Prerequisites & Environment Setup

### Prerequisites
* Java Development Kit (JDK) 17 installed
* Maven 3.8+ (optional, as Maven Wrapper `mvnw` is provided)
* Active MongoDB Atlas cluster connection string
* Postman (for API demonstration)

### Environment Variables
Configure the following environment variables before starting the services:

```bash
# Windows PowerShell
$env:MONGODB_URI="mongodb+srv://<username>:<password>@<cluster>.mongodb.net/?retryWrites=true&w=majority"
$env:JWT_SECRET="your-256-bit-secret-key-must-be-at-least-32-characters-long"
$env:DRIVER_SERVICE_TOKEN="your-optional-service-to-service-token"

# Linux / macOS / Bash
export MONGODB_URI="mongodb+srv://<username>:<password>@<cluster>.mongodb.net/?retryWrites=true&w=majority"
export JWT_SECRET="your-256-bit-secret-key-must-be-at-least-32-characters-long"
export DRIVER_SERVICE_TOKEN="your-optional-service-to-service-token"
```

---

## 5. How to Run Locally

### Recommended Startup Order
Because services integrate during runtime, start them in the following order:

1. **Account Service** (port 8080)
2. **Fare & Payment Service** (port 8083)
3. **Driver & Vehicle Service** (port 8081)
4. **Ride Management Service** (port 8082)

### Startup Commands

Open 4 separate terminal windows and run:

```bash
# Terminal 1 - Account Service
cd account-service
./mvnw spring-boot:run

# Terminal 2 - Fare & Payment Service
cd fare-payment-service
./mvnw spring-boot:run

# Terminal 3 - Driver & Vehicle Service
cd driver-service
./mvnw spring-boot:run

# Terminal 4 - Ride Management Service
cd ride-service
./mvnw spring-boot:run
```

*(On Windows Command Prompt or PowerShell, use `.\mvnw.cmd spring-boot:run`)*

---

## 6. Running Unit Tests

Each service has independent unit test suites covering normal flows, business logic validations, and negative cases.

You can run tests for each service using:

```bash
# Account Service (15 tests)
cd account-service && ./mvnw test

# Driver & Vehicle Service (10 tests)
cd driver-service && ./mvnw test

# Ride Management Service (9 tests)
cd ride-service && ./mvnw test

# Fare & Payment Service (18 tests)
cd fare-payment-service && ./mvnw test
```

---

## 7. Testing with Postman

A complete Postman Collection and Environment are provided in the `/postman` directory:
* `postman/RideLink.postman_collection.json`
* `postman/RideLink.postman_environment.json`

### Steps to Run:
1. Open Postman.
2. Click **Import** and select both files from the `postman/` directory.
3. In the top-right environment dropdown, select **RideLink Local Environment**.
4. Run the requests in order, or use **Postman Collection Runner** to run all folders automatically:
   * **1. Account & Authentication:** Registers passenger and driver, logs in, captures tokens.
   * **2. Driver & Vehicle Preparation:** Creates driver profile, registers vehicle, updates availability and simulated location.
   * **3. Ride Lifecycle & Assignment:** Creates ride request, calls fare estimation, assigns driver, moves through states (`ACCEPTED` $\rightarrow$ `IN_PROGRESS` $\rightarrow$ `COMPLETED`).
   * **4. Fare & Simulated Payment:** Calculates final fare, makes simulated payment, generates receipt.
   * **5. Negative Test Scenarios:** Tests invalid password, unauthorized tokenless access, invalid state transition, and 404 lookups.

### Default Test Credentials

| Role | Email | Password |
| :--- | :--- | :--- |
| **Passenger** | `kasun.passenger@ridelink.com` | `Password@123` |
| **Driver** | `saman.driver@ridelink.com` | `Password@123` |

---

## 8. Continuous Integration (CI)

Each microservice has its own dedicated GitHub Actions CI pipeline inside `.github/workflows/`:
* `account-service.yml`
* `driver-service.yml`
* `ride-service.yml`
* `fare-payment-service.yml`

Workflows run automatically when changes are pushed to `main` or `develop` branches that affect the respective service, or can be triggered manually via `workflow_dispatch`.
