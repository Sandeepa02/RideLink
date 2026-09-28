package com.ridelink.driverservice.service;

import com.ridelink.driverservice.model.Vehicle;
import com.ridelink.driverservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public List<Vehicle> getVehiclesByDriverId(String driverId) {
        return vehicleRepository.findByDriverId(driverId);
    }

    public Vehicle updateVehicle(String id, Vehicle updatedVehicle) {
        Vehicle existingVehicle = getVehicleById(id);

        existingVehicle.setDriverId(updatedVehicle.getDriverId());
        existingVehicle.setRegistrationNumber(
                updatedVehicle.getRegistrationNumber()
        );
        existingVehicle.setVehicleType(
                updatedVehicle.getVehicleType()
        );
        existingVehicle.setModel(updatedVehicle.getModel());
        existingVehicle.setColour(updatedVehicle.getColour());

        return vehicleRepository.save(existingVehicle);
    }

    public void deleteVehicle(String id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}