package com.ridelink.driverservice.repository;

import com.ridelink.driverservice.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    List<Vehicle> findByDriverId(String driverId);
}