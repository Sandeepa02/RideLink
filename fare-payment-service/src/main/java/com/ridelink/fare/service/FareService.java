package com.ridelink.fare.service;

import com.ridelink.fare.model.Fare;
import com.ridelink.fare.repository.FareRepository;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    private final FareRepository fareRepository;
    private final FareCalculationService fareCalculationService;

    public FareService(FareRepository fareRepository,
                       FareCalculationService fareCalculationService) {
        this.fareRepository = fareRepository;
        this.fareCalculationService = fareCalculationService;
    }

    public Fare createFare(String rideId,
                           double distanceKm,
                           int durationMinutes,
                           String type) {

        double baseFare = 100.0;
        double distanceCharge = distanceKm * 50.0;
        double timeCharge = durationMinutes * 10.0;

        double totalFare = fareCalculationService.calculateFare(
                distanceKm,
                durationMinutes
        );

        Fare fare = new Fare(
                rideId,
                distanceKm,
                durationMinutes,
                baseFare,
                distanceCharge,
                timeCharge,
                totalFare,
                type
        );

        return fareRepository.save(fare);
    }
}