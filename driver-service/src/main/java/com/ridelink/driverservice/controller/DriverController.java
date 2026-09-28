package com.ridelink.driverservice.controller;

import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(
            @Valid @RequestBody Driver driver) {

        return new ResponseEntity<>(
                driverService.createDriver(driver),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(
                driverService.getAllDrivers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                driverService.getDriverById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable String id,
            @Valid @RequestBody Driver driver) {

        return ResponseEntity.ok(
                driverService.updateDriver(id, driver)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable String id) {

        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        return ResponseEntity.ok(
                driverService.getAvailableDrivers()
        );
    }

    @GetMapping("/available/area")
    public ResponseEntity<List<Driver>> getAvailableDriversByArea(
            @RequestParam String serviceArea) {

        return ResponseEntity.ok(
                driverService.getAvailableDriversByArea(serviceArea)
        );
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Driver> updateAvailability(
            @PathVariable String id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                driverService.updateAvailability(id, status)
        );
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Driver> updateLocation(
            @PathVariable String id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        return ResponseEntity.ok(
                driverService.updateLocation(
                        id,
                        latitude,
                        longitude
                )
        );
    }
}