package com.ridelink.fare.service;

import com.ridelink.fare.model.Fare;
import com.ridelink.fare.repository.FareRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FareServiceTest {

    @Test
    void shouldCalculateAndSaveEstimateFare() {

        FareRepository repository = mock(FareRepository.class);
        FareCalculationService calculationService =
                new FareCalculationService();

        Fare savedFare = new Fare(
                "RID-001",
                8.5,
                22,
                100.0,
                425.0,
                220.0,
                745.0,
                "ESTIMATE"
        );

        when(repository.save(any(Fare.class))).thenReturn(savedFare);

        FareService service = new FareService(
                repository,
                calculationService
        );

        Fare result = service.createFare(
                "RID-001",
                8.5,
                22,
                "ESTIMATE"
        );

        assertEquals("RID-001", result.getRideId());
        assertEquals(8.5, result.getDistanceKm());
        assertEquals(22, result.getDurationMinutes());
        assertEquals(745.0, result.getTotalFare());
        assertEquals("ESTIMATE", result.getType());

        verify(repository, times(1)).save(any(Fare.class));
    }
}