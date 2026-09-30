package com.ridelink.fare.controller;

import com.ridelink.fare.dto.PaymentRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(
        name = "Payment Management",
        description = "APIs for payment processing, payment status, and receipt generation"
)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Process payment",
            description = "Processes a simulated payment for a ride and returns the payment status."
    )
    @PostMapping
    public PaymentResponse processPayment(
            @Valid @RequestBody PaymentRequest request) {

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

    @Operation(
            summary = "Get payment status",
            description = "Retrieves the details and current status of a payment using its payment ID."
    )
    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@PathVariable String paymentId) {

        Payment payment = paymentService.getPaymentById(paymentId);

        return new PaymentResponse(
                payment.getId(),
                payment.getRideId(),
                payment.getFareId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus()
        );
    }

    @Operation(
            summary = "Get payment receipt",
            description = "Generates and returns a receipt for a specific payment."
    )
    @GetMapping("/{paymentId}/receipt")
    public ReceiptResponse getReceipt(@PathVariable String paymentId) {

        return paymentService.generateReceipt(paymentId);
    }
}
