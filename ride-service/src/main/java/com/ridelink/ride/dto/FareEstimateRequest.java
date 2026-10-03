package com.ridelink.ride.dto;

public class FareEstimateRequest {

    private String rideId;
    private Double distanceKm;
    private Integer estimatedDurationMinutes;

    public FareEstimateRequest() {
    }

    public FareEstimateRequest(
            String rideId,
            Double distanceKm,
            Integer estimatedDurationMinutes) {

        this.rideId = rideId;
        this.distanceKm = distanceKm;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
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