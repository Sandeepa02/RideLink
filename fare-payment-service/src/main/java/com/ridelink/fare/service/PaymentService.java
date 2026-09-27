package com.ridelink.fare.service;

import com.ridelink.fare.model.Payment;
import com.ridelink.fare.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(String rideId,
                                  String fareId,
                                  double amount,
                                  String paymentMethod) {

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
                        new RuntimeException("Payment not found: " + paymentId)
                );
    }
}
