package com.ridelink.ride.service;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.exception.InvalidRideStatusException;

import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.LocationRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.dto.UpdateRideStatusRequest;
import com.ridelink.ride.model.Location;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;  
import com.ridelink.ride.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.dto.DriverAssignmentRequest;
import com.ridelink.ride.dto.DriverAssignmentResponse;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.FareEstimateRequest;
import com.ridelink.ride.dto.FareEstimateResponse;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;
    private final FareServiceClient fareServiceClient;

    public RideService(
        RideRepository rideRepository,
        DriverServiceClient driverServiceClient,
        FareServiceClient fareServiceClient) {

    this.rideRepository = rideRepository;
    this.driverServiceClient = driverServiceClient;
    this.fareServiceClient = fareServiceClient;
    }

    public RideResponse createRide(
        CreateRideRequest request,
        String authorizationHeader) {

    Ride ride = new Ride();

    ride.setPassengerId(request.getPassengerId());
    ride.setPickupLocation(toLocation(request.getPickupLocation()));
    ride.setDestinationLocation(toLocation(request.getDestinationLocation()));

    ride.setDistanceKm(request.getDistanceKm());
    ride.setEstimatedDurationMinutes(
            request.getEstimatedDurationMinutes());

    ride.setStatus(RideStatus.REQUESTED);
    ride.setRequestedAt(LocalDateTime.now());

    // Save first so MongoDB generates the ride ID.
    Ride savedRide = rideRepository.save(ride);

    FareEstimateRequest fareRequest =
            new FareEstimateRequest(
                    savedRide.getId(),
                    savedRide.getDistanceKm(),
                    savedRide.getEstimatedDurationMinutes()
            );

    FareEstimateResponse fareResponse =
            fareServiceClient.estimateFare(
                    fareRequest,
                    authorizationHeader
            );

    savedRide.setFareId(fareResponse.getFareId());
    savedRide.setEstimatedFare(fareResponse.getEstimatedFare());

    Ride updatedRide = rideRepository.save(savedRide);

    return toResponse(updatedRide);
}

    public RideResponse assignDriver(String id) {

    Ride ride = rideRepository.findById(id)
            .orElseThrow(() ->
                    new RideNotFoundException("Ride not found with id: " + id));

    if (ride.getStatus() != RideStatus.REQUESTED) {
        throw new InvalidRideStatusException(
                "Driver can only be assigned to a REQUESTED ride");
    }

    DriverAssignmentRequest request =
            new DriverAssignmentRequest(
                    ride.getId(),
                    ride.getPickupLocation().getLatitude(),
                    ride.getPickupLocation().getLongitude()
            );

   DriverAssignmentResponse assignment =
        driverServiceClient.assignDriver(request);

    ride.setDriverId(assignment.getDriverId());
    ride.setStatus(RideStatus.ASSIGNED);

    Ride updatedRide = rideRepository.save(ride);

    return toResponse(updatedRide);
}

    public RideResponse getRideById(String id) {

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + id));

        return toResponse(ride);
    }

    public List<RideResponse> getRidesByPassenger(String passengerId) {

        return rideRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RideResponse> getRidesByDriver(String driverId) {

        return rideRepository.findByDriverId(driverId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RideResponse updateRideStatus(
            String id,
            UpdateRideStatusRequest request) {

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with id: " + id));

        RideStatus currentStatus = ride.getStatus();
        RideStatus newStatus = request.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        ride.setStatus(newStatus);
        updateTimestamp(ride, newStatus);

        Ride updatedRide = rideRepository.save(ride);

        return toResponse(updatedRide);
    }

    private void validateStatusTransition(
            RideStatus currentStatus,
            RideStatus newStatus) {

        boolean validTransition = switch (currentStatus) {

            case REQUESTED ->
                    newStatus == RideStatus.ASSIGNED
                            || newStatus == RideStatus.CANCELLED;

            case ASSIGNED ->
                    newStatus == RideStatus.ACCEPTED
                            || newStatus == RideStatus.CANCELLED;

            case ACCEPTED ->
                    newStatus == RideStatus.IN_PROGRESS
                            || newStatus == RideStatus.CANCELLED;

            case IN_PROGRESS ->
                    newStatus == RideStatus.COMPLETED;

            case COMPLETED, CANCELLED ->
                    false;
        };

        if (!validTransition) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition from "
                            + currentStatus + " to " + newStatus);
        }
    }

    private void updateTimestamp(Ride ride, RideStatus status) {

        LocalDateTime now = LocalDateTime.now();

        switch (status) {

            case ACCEPTED -> ride.setAcceptedAt(now);

            case IN_PROGRESS -> ride.setStartedAt(now);

            case COMPLETED -> ride.setCompletedAt(now);

            case CANCELLED -> ride.setCancelledAt(now);

            default -> {
                // No timestamp required for REQUESTED or ASSIGNED.
            }
        }
    }

    private Location toLocation(LocationRequest request) {

        return new Location(
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude()
        );
    }

    private RideResponse toResponse(Ride ride) {

        RideResponse response = new RideResponse();

        response.setId(ride.getId());
        response.setPassengerId(ride.getPassengerId());
        response.setDriverId(ride.getDriverId());

        response.setPickupLocation(
                toLocationRequest(ride.getPickupLocation()));

        response.setDestinationLocation(
                toLocationRequest(ride.getDestinationLocation()));

        response.setStatus(ride.getStatus());
        response.setEstimatedFare(ride.getEstimatedFare());
        response.setFinalFare(ride.getFinalFare());
        response.setDistanceKm(ride.getDistanceKm());
        response.setEstimatedDurationMinutes(ride.getEstimatedDurationMinutes());
        response.setFareId(ride.getFareId());

        response.setRequestedAt(ride.getRequestedAt());
        response.setAcceptedAt(ride.getAcceptedAt());
        response.setStartedAt(ride.getStartedAt());
        response.setCompletedAt(ride.getCompletedAt());
        response.setCancelledAt(ride.getCancelledAt());

        return response;
    }

    private LocationRequest toLocationRequest(Location location) {

        if (location == null) {
            return null;
        }

        return new LocationRequest(
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude()
        );
    }
}