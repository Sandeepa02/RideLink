package com.ridelink.driverservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-for-driver-service-123456789"
})
class DriverServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}