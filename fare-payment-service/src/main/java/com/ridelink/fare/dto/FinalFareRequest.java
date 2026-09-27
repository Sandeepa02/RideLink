package com.ridelink.fare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FinalFareRequest {

    @NotBlank(message = "Ride ID is required")
    private String rideId;

    @Positive(message = "Distance must be greater than zero")
    private double distanceKm;

    @Positive(message = "Duration must be greater than zero")
    private int durationMinutes;

    public FinalFareRequest() {}

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}