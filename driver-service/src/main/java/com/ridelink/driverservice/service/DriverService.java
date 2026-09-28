package com.ridelink.driverservice.service;

import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.repository.DriverRepository;
import org.springframework.stereotype.Service;

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
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
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

        return driverRepository.save(driver);
    }

    public Driver updateLocation(String id, Double latitude, Double longitude) {
        Driver driver = getDriverById(id);

        driver.setLatitude(latitude);
        driver.setLongitude(longitude);

        return driverRepository.save(driver);
    }
}