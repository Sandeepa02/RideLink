package com.ridelink.fare.service;

import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.exception.ResourceNotFoundException;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.repository.FareRepository;
import com.ridelink.fare.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FareRepository fareRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            FareRepository fareRepository) {

        this.paymentRepository = paymentRepository;
        this.fareRepository = fareRepository;
    }

    public Payment processPayment(
            String rideId,
            String fareId,
            double amount,
            String paymentMethod) {

        fareRepository.findById(fareId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fare not found: " + fareId
                        )
                );

        String status;

        if (amount <= 0) {
            status = "FAILED";
        } else {
            status = "SUCCESS";
        }

        Payment payment = new Payment(
                rideId,
                fareId,
                amount,
                paymentMethod,
                status,
                LocalDateTime.now()
        );

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(String paymentId) {

        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found: " + paymentId
                        )
                );
    }

    public ReceiptResponse generateReceipt(String paymentId) {

        Payment payment = getPaymentById(paymentId);

        String receiptMessage;

        if ("SUCCESS".equals(payment.getStatus())) {
            receiptMessage = "Payment completed successfully";
        } else {
            receiptMessage = "Payment was not completed";
        }

        return new ReceiptResponse(
                payment.getId(),
                payment.getRideId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                receiptMessage
        );
    }
}