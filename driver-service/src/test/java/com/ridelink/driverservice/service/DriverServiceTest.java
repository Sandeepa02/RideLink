package com.ridelink.driverservice.service;

import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.repository.DriverRepository;
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
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Test
    void createDriver_shouldSaveDriver() {

        Driver driver = new Driver(
                "ACC001",
                "Kamal Perera",
                "0771234567",
                "AVAILABLE",
                6.9271,
                79.8612,
                "Colombo"
        );

        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.createDriver(driver);

        assertNotNull(result);
        assertEquals("Kamal Perera", result.getName());

        verify(driverRepository).save(driver);
    }

    @Test
    void getDriverById_shouldReturnDriver() {

        Driver driver = new Driver(
                "ACC001",
                "Kamal Perera",
                "0771234567",
                "AVAILABLE",
                6.9271,
                79.8612,
                "Colombo"
        );

        when(driverRepository.findById("D001"))
                .thenReturn(Optional.of(driver));

        Driver result = driverService.getDriverById("D001");

        assertNotNull(result);
        assertEquals("Kamal Perera", result.getName());

        verify(driverRepository).findById("D001");
    }

    @Test
    void getAvailableDrivers_shouldReturnAvailableDrivers() {

        Driver driver = new Driver(
                "ACC001",
                "Kamal Perera",
                "0771234567",
                "AVAILABLE",
                6.9271,
                79.8612,
                "Colombo"
        );

        when(driverRepository.findByAvailabilityStatus("AVAILABLE"))
                .thenReturn(List.of(driver));

        List<Driver> result = driverService.getAvailableDrivers();

        assertEquals(1, result.size());
        assertEquals("AVAILABLE", result.get(0).getAvailabilityStatus());

        verify(driverRepository)
                .findByAvailabilityStatus("AVAILABLE");
    }

    @Test
    void updateAvailability_shouldUpdateStatus() {

        Driver driver = new Driver(
                "ACC001",
                "Kamal Perera",
                "0771234567",
                "AVAILABLE",
                6.9271,
                79.8612,
                "Colombo"
        );

        when(driverRepository.findById("D001"))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(driver))
                .thenReturn(driver);

        Driver result =
                driverService.updateAvailability("D001", "UNAVAILABLE");

        assertEquals("UNAVAILABLE", result.getAvailabilityStatus());

        verify(driverRepository).save(driver);
    }

    @Test
    void updateLocation_shouldUpdateCoordinates() {

        Driver driver = new Driver(
                "ACC001",
                "Kamal Perera",
                "0771234567",
                "AVAILABLE",
                6.9271,
                79.8612,
                "Colombo"
        );

        when(driverRepository.findById("D001"))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(driver))
                .thenReturn(driver);

        Driver result =
                driverService.updateLocation(
                        "D001",
                        6.9147,
                        79.9725
                );

        assertEquals(6.9147, result.getLatitude());
        assertEquals(79.9725, result.getLongitude());

        verify(driverRepository).save(driver);
    }
}