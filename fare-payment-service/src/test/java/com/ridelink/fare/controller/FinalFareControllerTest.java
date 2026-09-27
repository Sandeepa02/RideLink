package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.service.FareCalculationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinalFareControllerTest {

    @Test
    void shouldCalculateFinalFare() {

        FareCalculationService service = new FareCalculationService();
        FareController controller = new FareController(service);

        FinalFareRequest request = new FinalFareRequest();
        request.setRideId("RID-001");
        request.setDistanceKm(8.7);
        request.setDurationMinutes(25);

        FinalFareResponse response = controller.calculateFinalFare(request);

        assertEquals("RID-001", response.getRideId());
        assertEquals(785.0, response.getFinalFare());
    }
}
