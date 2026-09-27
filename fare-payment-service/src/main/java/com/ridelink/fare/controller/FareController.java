package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.service.FareCalculationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareCalculationService fareCalculationService;

    public FareController(FareCalculationService fareCalculationService) {
        this.fareCalculationService = fareCalculationService;
    }


    @PostMapping("/final")
    public FinalFareResponse calculateFinalFare(
            @RequestBody FinalFareRequest request) {

        double finalFare = fareCalculationService.calculateFare(
                request.getDistanceKm(),
                request.getDurationMinutes()
        );

        return new FinalFareResponse(
                request.getRideId(),
                finalFare
        );
    }

    @PostMapping("/estimate")
    public FareEstimateResponse estimateFare(
            @RequestBody FareEstimateRequest request) {

        double estimatedFare = fareCalculationService.calculateFare(
                request.getDistanceKm(),
                request.getEstimatedDurationMinutes()
        );

        return new FareEstimateResponse(
                request.getRideId(),
                estimatedFare
        );
    }
}
