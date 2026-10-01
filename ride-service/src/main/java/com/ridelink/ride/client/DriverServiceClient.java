package com.ridelink.ride.client;

import com.ridelink.ride.dto.DriverAssignmentRequest;
import com.ridelink.ride.dto.DriverAssignmentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

@Component
public class DriverServiceClient {

    private final RestClient restClient;
    private final String serviceToken;

    public DriverServiceClient(
        @Value("${driver.service.base-url}") String baseUrl,
        @Value("${driver.service.token}") String serviceToken) {

    this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();

    this.serviceToken = serviceToken;
}

    public DriverAssignmentResponse assignDriver(
        DriverAssignmentRequest request) {

        return restClient.post()
                .uri("/api/drivers/assign")
               .header(
        HttpHeaders.AUTHORIZATION,
        "Bearer " + serviceToken
)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(DriverAssignmentResponse.class);
    }
}