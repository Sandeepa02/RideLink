package com.ridelink.fare.dto;

public class FinalFareResponse {

    private String rideId;
    private double finalFare;

    public FinalFareResponse() {
    }

    public FinalFareResponse(String rideId, double finalFare) {
        this.rideId = rideId;
        this.finalFare = finalFare;
    }

    public String getRideId() {
        return rideId;
    }

    public double getFinalFare() {
        return finalFare;
    }
}
