package com.ridelink.fare.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareEstimateRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldRejectInvalidFareEstimateRequest() {

        FareEstimateRequest request = new FareEstimateRequest();

        request.setRideId("");
        request.setDistanceKm(0);
        request.setEstimatedDurationMinutes(0);

        Set<ConstraintViolation<FareEstimateRequest>> violations =
                validator.validate(request);

        assertEquals(3, violations.size());
    }
}
