package com.ridelink.driverservice.dto;

public class DriverAssignmentResponse {

    private String driverId;
    private String rideId;
    private String availabilityStatus;

    public DriverAssignmentResponse(
            String driverId,
            String rideId,
            String availabilityStatus) {

        this.driverId = driverId;
        this.rideId = rideId;
        this.availabilityStatus = availabilityStatus;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getRideId() {
        return rideId;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
}
