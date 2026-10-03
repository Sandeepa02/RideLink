package com.ridelink.fare.repository;

import com.ridelink.fare.model.Fare;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FareRepository extends MongoRepository<Fare, String> {

    Optional<Fare> findByRideId(String rideId);
}
