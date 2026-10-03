package com.ridelink.ride.client;

import com.ridelink.ride.dto.FareEstimateRequest;
import com.ridelink.ride.dto.FareEstimateResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FareServiceClient {

    private final RestClient restClient;

    public FareServiceClient(
            @org.springframework.beans.factory.annotation.Value("${fare.service.base-url}")
            String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public FareEstimateResponse estimateFare(
            FareEstimateRequest request,
            String authorizationHeader) {

        return restClient.post()
                .uri("/api/fares/estimate")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(FareEstimateResponse.class);
    }
}