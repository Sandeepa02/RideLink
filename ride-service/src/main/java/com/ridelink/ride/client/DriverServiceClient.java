package com.ridelink.ride.client;

import com.ridelink.ride.dto.DriverAssignmentRequest;
import com.ridelink.ride.dto.DriverAssignmentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DriverServiceClient {

    private final RestClient restClient;

    public DriverServiceClient(
            @Value("${driver.service.base-url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public DriverAssignmentResponse assignDriver(
            DriverAssignmentRequest request,
            String serviceToken) {

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