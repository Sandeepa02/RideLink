package com.ridelink.driverservice.controller;

import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Driver> createDriver(
            @Valid @RequestBody Driver driver) {

        return new ResponseEntity<>(
                driverService.createDriver(driver),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(
                driverService.getAllDrivers()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Driver> getDriverById(
            @PathVariable String id) {

        Driver driver = driverService.getDriverById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)
                && !driver.getAccountId().equals(authentication.getName())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(driver);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Driver> updateDriver(
            @PathVariable String id,
            @Valid @RequestBody Driver driver) {

        Driver existingDriver = driverService.getDriverById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)
                && !existingDriver.getAccountId().equals(authentication.getName())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                driverService.updateDriver(id, driver)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable String id) {

        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        return ResponseEntity.ok(
                driverService.getAvailableDrivers()
        );
    }

    @GetMapping("/available/area")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public ResponseEntity<List<Driver>> getAvailableDriversByArea(
            @RequestParam String serviceArea) {

        return ResponseEntity.ok(
                driverService.getAvailableDriversByArea(serviceArea)
        );
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Driver> updateAvailability(
            @PathVariable String id,
            @RequestParam String status) {

        Driver existingDriver = driverService.getDriverById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)
                && !existingDriver.getAccountId().equals(authentication.getName())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                driverService.updateAvailability(id, status)
        );
    }

    @PutMapping("/{id}/location")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Driver> updateLocation(
            @PathVariable String id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        Driver existingDriver = driverService.getDriverById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)
                && !existingDriver.getAccountId().equals(authentication.getName())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(
                driverService.updateLocation(
                        id,
                        latitude,
                        longitude
                )
        );
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
