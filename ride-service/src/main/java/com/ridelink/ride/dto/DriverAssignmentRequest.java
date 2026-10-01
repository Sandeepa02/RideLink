package com.ridelink.ride.dto;

public class DriverAssignmentRequest {

    private String rideId;
    private Double pickupLatitude;
    private Double pickupLongitude;

    public DriverAssignmentRequest() {
    }

    public DriverAssignmentRequest(
            String rideId,
            Double pickupLatitude,
            Double pickupLongitude) {
        this.rideId = rideId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }
}