package com.ridelink.fare.dto;

public class FareEstimateResponse {

    private String fareId;
    private String rideId;
    private double estimatedFare;

    public FareEstimateResponse() {
    }

    public FareEstimateResponse(
            String fareId,
            String rideId,
            double estimatedFare) {

        this.fareId = fareId;
        this.rideId = rideId;
        this.estimatedFare = estimatedFare;
    }

    public String getFareId() {
        return fareId;
    }

    public void setFareId(String fareId) {
        this.fareId = fareId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }
}
