package com.ridelink.fare.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareCalculationServiceTest {

    private final FareCalculationService fareCalculationService =
            new FareCalculationService();

    @Test
    void shouldCalculateFareCorrectly() {

        double fare = fareCalculationService.calculateFare(8.5, 22);

        assertEquals(745.0, fare);
    }

    @Test
    void shouldCalculateBaseFareForZeroDistanceAndDuration() {

        double fare = fareCalculationService.calculateFare(0, 0);

        assertEquals(100.0, fare);
    }
}
