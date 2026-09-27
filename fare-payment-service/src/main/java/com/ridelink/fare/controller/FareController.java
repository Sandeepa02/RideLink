package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.model.Fare;
import com.ridelink.fare.service.FareService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/estimate")
    public FareEstimateResponse estimateFare(
            @RequestBody FareEstimateRequest request) {

        Fare fare = fareService.createFare(
                request.getRideId(),
                request.getDistanceKm(),
                request.getEstimatedDurationMinutes(),
                "ESTIMATE"
        );

        return new FareEstimateResponse(
                fare.getRideId(),
                fare.getTotalFare()
        );
    }

    @PostMapping("/final")
    public FinalFareResponse calculateFinalFare(
            @RequestBody FinalFareRequest request) {

        Fare fare = fareService.createFare(
                request.getRideId(),
                request.getDistanceKm(),
                request.getDurationMinutes(),
                "FINAL"
        );

        return new FinalFareResponse(
                fare.getRideId(),
                fare.getTotalFare()
        );
    }
}