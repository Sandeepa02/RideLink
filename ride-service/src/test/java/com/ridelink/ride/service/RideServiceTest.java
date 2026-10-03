package com.ridelink.ride.service;

import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.LocationRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.exception.InvalidRideStatusException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.FareEstimateRequest;
import com.ridelink.ride.dto.FareEstimateResponse;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private FareServiceClient fareServiceClient;

    @InjectMocks
    private RideService rideService;

    private CreateRideRequest createRequest;

    @BeforeEach
    void setUp() {
        LocationRequest pickup = new LocationRequest(
                "Colombo Fort",
                6.9344,
                79.8428
        );

        LocationRequest destination = new LocationRequest(
                "Bambalapitiya",
                6.8933,
                79.8560
        );

        createRequest = new CreateRideRequest(
                "passenger-001",
                pickup,
                destination,
                8.5,
                22
        );
    }

    @Test
    void shouldCreateRideWithRequestedStatus() {

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FareEstimateResponse fareResponse =
                new FareEstimateResponse();

        fareResponse.setFareId("fare-001");
        fareResponse.setEstimatedFare(745.0);

        when(fareServiceClient.estimateFare(
                any(FareEstimateRequest.class),
                eq("Bearer test-rider-token")
        )).thenReturn(fareResponse);

        RideResponse response =
                rideService.createRide(
                        createRequest,
                        "Bearer test-rider-token"
                );

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("passenger-001", response.getPassengerId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertEquals(8.5, response.getDistanceKm());
        assertEquals(22, response.getEstimatedDurationMinutes());
        assertEquals("fare-001", response.getFareId());
        assertEquals(745.0, response.getEstimatedFare());

        verify(fareServiceClient, times(1))
                .estimateFare(
                        any(FareEstimateRequest.class),
                        eq("Bearer test-rider-token")
                );

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void shouldReturnRideWhenRideExists() {

        Ride ride = new Ride();
        ride.setId("ride-001");
        ride.setPassengerId("passenger-001");
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        RideResponse response =
                rideService.getRideById("ride-001");

        assertEquals("ride-001", response.getId());
        assertEquals("passenger-001", response.getPassengerId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenRideDoesNotExist() {

        when(rideRepository.findById("missing-id"))
                .thenReturn(Optional.empty());

        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getRideById("missing-id")
        );
    }

    @Test
    void shouldAllowValidStatusTransition() {

        Ride ride = new Ride();
        ride.setId("ride-001");
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.updateRideStatus(
                "ride-001",
                new UpdateRideStatusRequest(RideStatus.ASSIGNED)
        );

        assertEquals(RideStatus.ASSIGNED, response.getStatus());

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void shouldRejectInvalidStatusTransition() {

        Ride ride = new Ride();
        ride.setId("ride-001");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        assertThrows(
                InvalidRideStatusException.class,
                () -> rideService.updateRideStatus(
                        "ride-001",
                        new UpdateRideStatusRequest(RideStatus.COMPLETED)
                )
        );

        verify(rideRepository, never())
                .save(any(Ride.class));
    }

    @Test
    void shouldSetAcceptedTimestampWhenRideIsAccepted() {

        Ride ride = new Ride();
        ride.setId("ride-001");
        ride.setStatus(RideStatus.ASSIGNED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.updateRideStatus(
                "ride-001",
                new UpdateRideStatusRequest(RideStatus.ACCEPTED)
        );

        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        assertNotNull(response.getAcceptedAt());
    }

    @Test
    void shouldCancelRideFromRequestedStatus() {

        Ride ride = new Ride();
        ride.setId("ride-001");
        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response = rideService.updateRideStatus(
                "ride-001",
                new UpdateRideStatusRequest(RideStatus.CANCELLED)
        );

        assertEquals(RideStatus.CANCELLED, response.getStatus());
        assertNotNull(response.getCancelledAt());
    }

    @Test
    void shouldNotSaveRideWhenFareServiceFails() {

        when(fareServiceClient.estimateFare(
                any(FareEstimateRequest.class),
                eq("Bearer test-rider-token")
        )).thenThrow(new RestClientException("Fare Service unavailable"));

        assertThrows(
                RestClientException.class,
                () -> rideService.createRide(
                        createRequest,
                        "Bearer test-rider-token"
                )
        );

        verify(fareServiceClient, times(1))
                .estimateFare(
                        any(FareEstimateRequest.class),
                        eq("Bearer test-rider-token")
                );

        verify(rideRepository, never())
                .save(any(Ride.class));
        }
}
