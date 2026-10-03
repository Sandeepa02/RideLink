package com.ridelink.fare.service;

import org.springframework.stereotype.Service;

@Service
public class FareCalculationService {

    private static final double BASE_FARE = 100.0;
    private static final double RATE_PER_KM = 50.0;
    private static final double RATE_PER_MINUTE = 10.0;

    public double calculateFare(double distanceKm, int durationMinutes) {

        double distanceCharge = distanceKm * RATE_PER_KM;
        double timeCharge = durationMinutes * RATE_PER_MINUTE;

        return BASE_FARE + distanceCharge + timeCharge;
    }
}
