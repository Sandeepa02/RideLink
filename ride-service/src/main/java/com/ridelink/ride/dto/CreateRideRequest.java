package com.ridelink.ride.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateRideRequest {

    @NotBlank(message = "Passenger ID is required")
    private String passengerId;

    @NotNull(message = "Pickup location is required")
    @Valid
    private LocationRequest pickupLocation;

    @NotNull(message = "Destination location is required")
    @Valid
    private LocationRequest destinationLocation;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be greater than 0")
    private Double distanceKm;

    @NotNull(message = "Estimated duration is required")
    @Positive(message = "Estimated duration must be greater than 0")
    private Integer estimatedDurationMinutes;

    public CreateRideRequest() {
    }

    // Existing constructor kept for backward compatibility with existing tests
    public CreateRideRequest(
            String passengerId,
            LocationRequest pickupLocation,
            LocationRequest destinationLocation) {

        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
    }

    // New constructor for Fare Service integration
    public CreateRideRequest(
            String passengerId,
            LocationRequest pickupLocation,
            LocationRequest destinationLocation,
            Double distanceKm,
            Integer estimatedDurationMinutes) {

        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.distanceKm = distanceKm;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public LocationRequest getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationRequest pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationRequest getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LocationRequest destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }
}