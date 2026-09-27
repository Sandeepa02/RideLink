package com.ridelink.fare.controller;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse processPayment(
            @RequestBody PaymentRequest request) {

        Payment payment = paymentService.processPayment(
                request.getRideId(),
                request.getFareId(),
                request.getAmount(),
                request.getPaymentMethod()
        );

        return new PaymentResponse(
                payment.getId(),
                payment.getRideId(),
                payment.getFareId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus()
        );
    }
}