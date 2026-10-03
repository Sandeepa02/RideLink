package com.ridelink.fare;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "JWT_SECRET=test-secret-for-context-loads-only",
        "MONGODB_URI=mongodb://localhost:27017/ridelink_test"
})
class FarePaymentServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
