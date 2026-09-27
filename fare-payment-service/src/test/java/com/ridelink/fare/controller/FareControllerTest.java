package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.model.Fare;
import com.ridelink.fare.service.FareService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FareControllerTest {

    @Test
    void shouldCalculateEstimatedFare() {

        FareService service = mock(FareService.class);

        Fare fare = new Fare(
                "RID-001",
                8.5,
                22,
                100.0,
                425.0,
                220.0,
                745.0,
                "ESTIMATE"
        );

        when(service.createFare(
                "RID-001",
                8.5,
                22,
                "ESTIMATE"
        )).thenReturn(fare);

        FareController controller = new FareController(service);

        FareEstimateRequest request = new FareEstimateRequest();
        request.setRideId("RID-001");
        request.setDistanceKm(8.5);
        request.setEstimatedDurationMinutes(22);

        FareEstimateResponse response = controller.estimateFare(request);

        assertEquals("RID-001", response.getRideId());
        assertEquals(745.0, response.getEstimatedFare());

        verify(service, times(1)).createFare(
                "RID-001",
                8.5,
                22,
                "ESTIMATE"
        );
    }
}