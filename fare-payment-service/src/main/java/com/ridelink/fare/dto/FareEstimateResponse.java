package com.ridelink.fare.dto;

public class FareEstimateResponse {

    private String rideId;
    private double estimatedFare;

    public FareEstimateResponse() {
    }

    public FareEstimateResponse(String rideId, double estimatedFare) {
        this.rideId = rideId;
        this.estimatedFare = estimatedFare;
    }

    public String getRideId() {
        return rideId;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }
}
