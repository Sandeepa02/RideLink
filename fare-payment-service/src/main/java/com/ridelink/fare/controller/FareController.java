package com.ridelink.fare.controller;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.dto.FinalFareRequest;
import com.ridelink.fare.dto.FinalFareResponse;
import com.ridelink.fare.model.Fare;
import com.ridelink.fare.service.FareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fares")
@Tag(
        name = "Fare Management",
        description = "APIs for fare estimation and final fare calculation"
)
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @Operation(
            summary = "Estimate fare",
            description = "Calculates an estimated fare using the estimated distance and duration."
        )
        @PostMapping("/estimate")
        public FareEstimateResponse estimateFare(
                @Valid @RequestBody FareEstimateRequest request) {

        Fare fare = fareService.createFare(
                request.getRideId(),
                request.getDistanceKm(),
                request.getEstimatedDurationMinutes(),
                "ESTIMATE"
        );

        return new FareEstimateResponse(
                fare.getId(),
                fare.getRideId(),
                fare.getTotalFare()
        );
    }

    @Operation(
            summary = "Calculate final fare",
            description = "Calculates the final fare using the actual distance and duration."
        )
        @PostMapping("/final")
        public FinalFareResponse calculateFinalFare(
                @Valid @RequestBody FinalFareRequest request) {

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
