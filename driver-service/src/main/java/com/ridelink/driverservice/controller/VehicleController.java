package com.ridelink.driverservice.controller;

import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.model.Vehicle;
import com.ridelink.driverservice.service.DriverService;
import com.ridelink.driverservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final DriverService driverService;

    public VehicleController(
            VehicleService vehicleService,
            DriverService driverService) {

        this.vehicleService = vehicleService;
        this.driverService = driverService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Vehicle> createVehicle(
            @Valid @RequestBody Vehicle vehicle) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)) {

            Driver driver =
                    driverService.getDriverById(vehicle.getDriverId());

            if (!driver.getAccountId().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return new ResponseEntity<>(
                vehicleService.createVehicle(vehicle),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        return ResponseEntity.ok(
                vehicleService.getAllVehicles()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Vehicle> getVehicleById(
            @PathVariable String id) {

        Vehicle vehicle =
                vehicleService.getVehicleById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)) {

            Driver driver =
                    driverService.getDriverById(vehicle.getDriverId());

            if (!driver.getAccountId().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/driver/{driverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<List<Vehicle>> getVehiclesByDriverId(
            @PathVariable String driverId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)) {

            Driver driver =
                    driverService.getDriverById(driverId);

            if (!driver.getAccountId().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(
                vehicleService.getVehiclesByDriverId(driverId)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable String id,
            @Valid @RequestBody Vehicle vehicle) {

        Vehicle existingVehicle =
                vehicleService.getVehicleById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)) {

            Driver driver =
                    driverService.getDriverById(existingVehicle.getDriverId());

            if (!driver.getAccountId().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(
                vehicleService.updateVehicle(id, vehicle)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable String id) {

        Vehicle existingVehicle =
                vehicleService.getVehicleById(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!isAdmin(authentication)) {

            Driver driver =
                    driverService.getDriverById(existingVehicle.getDriverId());

            if (!driver.getAccountId().equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        vehicleService.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
