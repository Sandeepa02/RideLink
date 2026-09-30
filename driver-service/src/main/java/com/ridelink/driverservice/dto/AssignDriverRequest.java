package com.ridelink.driverservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssignDriverRequest {

    @NotBlank(message = "rideId is required")
    private String rideId;

    @NotNull(message = "pickupLatitude is required")
    @DecimalMin(
            value = "-90.0",
            message = "pickupLatitude must be between -90 and 90"
    )
    @DecimalMax(
            value = "90.0",
            message = "pickupLatitude must be between -90 and 90"
    )
    private Double pickupLatitude;

    @NotNull(message = "pickupLongitude is required")
    @DecimalMin(
            value = "-180.0",
            message = "pickupLongitude must be between -180 and 180"
    )
    @DecimalMax(
            value = "180.0",
            message = "pickupLongitude must be between -180 and 180"
    )
    private Double pickupLongitude;

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
