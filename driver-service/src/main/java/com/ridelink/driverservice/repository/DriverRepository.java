package com.ridelink.driverservice.repository;

import com.ridelink.driverservice.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DriverRepository extends MongoRepository<Driver, String> {

    List<Driver> findByAvailabilityStatus(String availabilityStatus);

    List<Driver> findByServiceAreaAndAvailabilityStatus(
            String serviceArea,
            String availabilityStatus
    );
}