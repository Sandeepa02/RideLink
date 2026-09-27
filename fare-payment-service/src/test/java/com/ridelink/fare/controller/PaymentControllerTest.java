package com.ridelink.fare.controller;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.service.PaymentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

class PaymentControllerTest {

    @Test
    void shouldProcessPayment() {

        PaymentService service = mock(PaymentService.class);

        Payment payment = new Payment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD",
                "SUCCESS",
                LocalDateTime.now()
        );

        when(service.processPayment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD"
        )).thenReturn(payment);

        PaymentController controller = new PaymentController(service);

        PaymentRequest request = new PaymentRequest();
        request.setRideId("RID-001");
        request.setFareId("FARE-001");
        request.setAmount(785.0);
        request.setPaymentMethod("CARD");

        PaymentResponse response = controller.processPayment(request);

        assertEquals("RID-001", response.getRideId());
        assertEquals("FARE-001", response.getFareId());
        assertEquals(785.0, response.getAmount());
        assertEquals("CARD", response.getPaymentMethod());
        assertEquals("SUCCESS", response.getStatus());

        verify(service, times(1)).processPayment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD"
        );
    }

    @Test
    void shouldRetrievePaymentById() {

        PaymentService service = mock(PaymentService.class);

        Payment payment = new Payment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD",
                "SUCCESS",
                LocalDateTime.now()
        );

        when(service.getPaymentById("PAY-001"))
                .thenReturn(payment);

        PaymentController controller = new PaymentController(service);

        PaymentResponse response =
                controller.getPayment("PAY-001");

        assertEquals("RID-001", response.getRideId());
        assertEquals("FARE-001", response.getFareId());
        assertEquals(785.0, response.getAmount());
        assertEquals("CARD", response.getPaymentMethod());
        assertEquals("SUCCESS", response.getStatus());

        verify(service, times(1))
                .getPaymentById("PAY-001");
    }
}
