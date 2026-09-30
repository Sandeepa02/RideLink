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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

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
                destination
        );
    }

    @Test
    void shouldCreateRideWithRequestedStatus() {

        Ride savedRide = new Ride();
        savedRide.setId("ride-001");
        savedRide.setPassengerId("passenger-001");
        savedRide.setStatus(RideStatus.REQUESTED);

        when(rideRepository.save(any(Ride.class)))
                .thenReturn(savedRide);

        RideResponse response = rideService.createRide(createRequest);

        assertNotNull(response);
        assertEquals("ride-001", response.getId());
        assertEquals("passenger-001", response.getPassengerId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());

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
}