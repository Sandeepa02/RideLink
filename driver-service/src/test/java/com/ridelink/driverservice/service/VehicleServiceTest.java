package com.ridelink.driverservice.service;

import com.ridelink.driverservice.model.Vehicle;
import com.ridelink.driverservice.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_shouldSaveVehicle() {

        Vehicle vehicle = new Vehicle(
                "D001",
                "CAB-1234",
                "CAR",
                "Toyota Prius",
                "White"
        );

        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle result = vehicleService.createVehicle(vehicle);

        assertNotNull(result);
        assertEquals("CAB-1234", result.getRegistrationNumber());

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void getVehicleById_shouldReturnVehicle() {

        Vehicle vehicle = new Vehicle(
                "D001",
                "CAB-1234",
                "CAR",
                "Toyota Prius",
                "White"
        );

        when(vehicleRepository.findById("V001"))
                .thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicleById("V001");

        assertNotNull(result);
        assertEquals("Toyota Prius", result.getModel());

        verify(vehicleRepository).findById("V001");
    }

    @Test
    void getVehiclesByDriverId_shouldReturnVehicles() {

        Vehicle vehicle = new Vehicle(
                "D001",
                "CAB-1234",
                "CAR",
                "Toyota Prius",
                "White"
        );

        when(vehicleRepository.findByDriverId("D001"))
                .thenReturn(List.of(vehicle));

        List<Vehicle> result =
                vehicleService.getVehiclesByDriverId("D001");

        assertEquals(1, result.size());
        assertEquals("D001", result.get(0).getDriverId());

        verify(vehicleRepository).findByDriverId("D001");
    }

    @Test
    void updateVehicle_shouldUpdateVehicleDetails() {

        Vehicle existingVehicle = new Vehicle(
                "D001",
                "CAB-1234",
                "CAR",
                "Toyota Prius",
                "White"
        );

        Vehicle updatedVehicle = new Vehicle(
                "D001",
                "CAB-1234",
                "CAR",
                "Toyota Prius",
                "Black"
        );

        when(vehicleRepository.findById("V001"))
                .thenReturn(Optional.of(existingVehicle));

        when(vehicleRepository.save(existingVehicle))
                .thenReturn(existingVehicle);

        Vehicle result =
                vehicleService.updateVehicle(
                        "V001",
                        updatedVehicle
                );

        assertEquals("Black", result.getColour());

        verify(vehicleRepository).save(existingVehicle);
    }
}