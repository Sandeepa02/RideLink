package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.model.Fare;
import com.ridelink.fare.service.FareService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FinalFareControllerTest {

    @Test
    void shouldCalculateFinalFare() {

        FareService service = mock(FareService.class);

        Fare fare = new Fare(
                "RID-001",
                8.7,
                25,
                100.0,
                435.0,
                250.0,
                785.0,
                "FINAL"
        );

        when(service.createFare(
                "RID-001",
                8.7,
                25,
                "FINAL"
        )).thenReturn(fare);

        FareController controller = new FareController(service);

        FinalFareRequest request = new FinalFareRequest();
        request.setRideId("RID-001");
        request.setDistanceKm(8.7);
        request.setDurationMinutes(25);

        FinalFareResponse response = controller.calculateFinalFare(request);

        assertEquals("RID-001", response.getRideId());
        assertEquals(785.0, response.getFinalFare());

        verify(service, times(1)).createFare(
                "RID-001",
                8.7,
                25,
                "FINAL"
        );
    }
}