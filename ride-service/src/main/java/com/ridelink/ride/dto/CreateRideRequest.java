package com.ridelink.ride.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateRideRequest {

    @NotBlank(message = "Passenger ID is required")
    private String passengerId;

    @NotNull(message = "Pickup location is required")
    @Valid
    private LocationRequest pickupLocation;

    @NotNull(message = "Destination location is required")
    @Valid
    private LocationRequest destinationLocation;

    public CreateRideRequest() {
    }

    public CreateRideRequest(
            String passengerId,
            LocationRequest pickupLocation,
            LocationRequest destinationLocation) {

        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
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
}