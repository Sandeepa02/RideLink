package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.service.FareCalculationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareControllerTest {

    @Test
    void shouldReturnEstimatedFare() {

        FareCalculationService service = new FareCalculationService();
        FareController controller = new FareController(service);

        FareEstimateRequest request = new FareEstimateRequest();
        request.setRideId("RID-001");
        request.setDistanceKm(8.5);
        request.setEstimatedDurationMinutes(22);

        FareEstimateResponse response = controller.estimateFare(request);

        assertEquals("RID-001", response.getRideId());
        assertEquals(745.0, response.getEstimatedFare());
    }
}
