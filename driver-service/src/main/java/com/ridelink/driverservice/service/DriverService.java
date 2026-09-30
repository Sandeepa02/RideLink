package com.ridelink.driverservice.service;

import com.ridelink.driverservice.dto.AssignDriverRequest;
import com.ridelink.driverservice.dto.DriverAssignmentResponse;
import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver not found"));
    }

    public Driver updateDriver(String id, Driver updatedDriver) {
        Driver existingDriver = getDriverById(id);

        existingDriver.setAccountId(updatedDriver.getAccountId());
        existingDriver.setName(updatedDriver.getName());
        existingDriver.setPhone(updatedDriver.getPhone());
        existingDriver.setAvailabilityStatus(
                updatedDriver.getAvailabilityStatus()
        );
        existingDriver.setLatitude(updatedDriver.getLatitude());
        existingDriver.setLongitude(updatedDriver.getLongitude());
        existingDriver.setServiceArea(updatedDriver.getServiceArea());

        return driverRepository.save(existingDriver);
    }

    public void deleteDriver(String id) {
        Driver driver = getDriverById(id);
        driverRepository.delete(driver);
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailabilityStatus("AVAILABLE");
    }

    public List<Driver> getAvailableDriversByArea(String serviceArea) {
        return driverRepository.findByServiceAreaAndAvailabilityStatus(
                serviceArea,
                "AVAILABLE"
        );
    }

    public Driver updateAvailability(String id, String status) {
        Driver driver = getDriverById(id);

        driver.setAvailabilityStatus(status);

        if ("AVAILABLE".equalsIgnoreCase(status)) {
            driver.setCurrentRideId(null);
        }

        return driverRepository.save(driver);
    }

    public Driver updateLocation(
            String id,
            Double latitude,
            Double longitude) {

        Driver driver = getDriverById(id);

        driver.setLatitude(latitude);
        driver.setLongitude(longitude);

        return driverRepository.save(driver);
    }

    public DriverAssignmentResponse assignDriver(
            AssignDriverRequest request) {

        List<Driver> availableDrivers =
                driverRepository.findByAvailabilityStatus("AVAILABLE");

        if (availableDrivers.isEmpty()) {
            throw new IllegalStateException(
                    "No available driver found"
            );
        }

        availableDrivers.sort(
                Comparator.comparingDouble(driver ->
                        calculateDistance(
                                request.getPickupLatitude(),
                                request.getPickupLongitude(),
                                driver.getLatitude(),
                                driver.getLongitude()
                        )
                )
        );

        for (Driver driver : availableDrivers) {

            long updatedCount =
                    driverRepository.reserveDriver(
                            driver.getId(),
                            request.getRideId()
                    );

            if (updatedCount == 1) {

                return new DriverAssignmentResponse(
                        driver.getId(),
                        request.getRideId(),
                        "BUSY"
                );
            }
        }

        throw new IllegalStateException(
                "No available driver found"
        );
    }

    private double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        double earthRadiusKm = 6371.0;

        double latitudeDifference =
                Math.toRadians(latitude2 - latitude1);

        double longitudeDifference =
                Math.toRadians(longitude2 - longitude1);

        double a =
                Math.sin(latitudeDifference / 2)
                        * Math.sin(latitudeDifference / 2)
                        + Math.cos(Math.toRadians(latitude1))
                        * Math.cos(Math.toRadians(latitude2))
                        * Math.sin(longitudeDifference / 2)
                        * Math.sin(longitudeDifference / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return earthRadiusKm * c;
    }
}
