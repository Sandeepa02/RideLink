package com.ridelink.driverservice.repository;

import com.ridelink.driverservice.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface DriverRepository extends MongoRepository<Driver, String> {

    List<Driver> findByAvailabilityStatus(String availabilityStatus);

    List<Driver> findByServiceAreaAndAvailabilityStatus(
            String serviceArea,
            String availabilityStatus
    );

    @Query("{ '_id': ?0, 'availabilityStatus': 'AVAILABLE' }")
    @Update("{ '$set': { 'availabilityStatus': 'BUSY', 'currentRideId': ?1 } }")
    long reserveDriver(String driverId, String rideId);
}
