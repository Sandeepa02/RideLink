package com.ridelink.ride.controller;

import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<RideResponse> createRide(
            @Valid @RequestBody CreateRideRequest request) {

        RideResponse response = rideService.createRide(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                rideService.getRideById(id)
        );
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<RideResponse>> getRidesByPassenger(
            @PathVariable String passengerId) {

        return ResponseEntity.ok(
                rideService.getRidesByPassenger(passengerId)
        );
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RideResponse>> getRidesByDriver(
            @PathVariable String driverId) {

        return ResponseEntity.ok(
                rideService.getRidesByDriver(driverId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RideResponse> updateRideStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateRideStatusRequest request) {

        return ResponseEntity.ok(
                rideService.updateRideStatus(id, request)
        );
    }

    @PostMapping("/{id}/assign")
public ResponseEntity<RideResponse> assignDriver(
        @PathVariable String id) {

    return ResponseEntity.ok(
            rideService.assignDriver(id)
    );
}

}