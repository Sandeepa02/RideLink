package com.ridelink.driverservice.controller;

import com.ridelink.driverservice.model.Vehicle;
import com.ridelink.driverservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(
            @Valid @RequestBody Vehicle vehicle) {

        return new ResponseEntity<>(
                vehicleService.createVehicle(vehicle),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(
                vehicleService.getAllVehicles()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                vehicleService.getVehicleById(id)
        );
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByDriverId(
            @PathVariable String driverId) {

        return ResponseEntity.ok(
                vehicleService.getVehiclesByDriverId(driverId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable String id,
            @Valid @RequestBody Vehicle vehicle) {

        return ResponseEntity.ok(
                vehicleService.updateVehicle(id, vehicle)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable String id) {

        vehicleService.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }
}