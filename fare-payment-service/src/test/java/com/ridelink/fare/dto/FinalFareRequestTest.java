package com.ridelink.fare.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinalFareRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldRejectInvalidFinalFareRequest() {

        FinalFareRequest request = new FinalFareRequest();

        request.setRideId("");
        request.setDistanceKm(0);
        request.setDurationMinutes(0);

        Set<ConstraintViolation<FinalFareRequest>> violations =
                validator.validate(request);

        assertEquals(3, violations.size());
    }
}