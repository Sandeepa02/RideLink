package com.ridelink.fare.service;

import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.repository.PaymentRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Test
    void shouldProcessSuccessfulPayment() {

        PaymentRepository repository = mock(PaymentRepository.class);

        Payment savedPayment = new Payment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD",
                "SUCCESS",
                null
        );

        when(repository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentService service = new PaymentService(repository);

        Payment result = service.processPayment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD"
        );

        assertEquals("RID-001", result.getRideId());
        assertEquals("FARE-001", result.getFareId());
        assertEquals(785.0, result.getAmount());
        assertEquals("CARD", result.getPaymentMethod());
        assertEquals("SUCCESS", result.getStatus());

        verify(repository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldFailPaymentWhenAmountIsZero() {

        PaymentRepository repository = mock(PaymentRepository.class);

        Payment savedPayment = new Payment(
                "RID-002",
                "FARE-002",
                0.0,
                "CARD",
                "FAILED",
                null
        );

        when(repository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentService service = new PaymentService(repository);

        Payment result = service.processPayment(
                "RID-002",
                "FARE-002",
                0.0,
                "CARD"
        );

        assertEquals("FAILED", result.getStatus());

        verify(repository, times(1)).save(any(Payment.class));
    }
    
    @Test
    void shouldRetrievePaymentById() {

        PaymentRepository repository = mock(PaymentRepository.class);

        Payment payment = new Payment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD",
                "SUCCESS",
                null
        );

        when(repository.findById("PAY-001"))
                .thenReturn(java.util.Optional.of(payment));

        PaymentService service = new PaymentService(repository);

        Payment result = service.getPaymentById("PAY-001");

        assertEquals("RID-001", result.getRideId());
        assertEquals("FARE-001", result.getFareId());
        assertEquals(785.0, result.getAmount());
        assertEquals("SUCCESS", result.getStatus());

        verify(repository, times(1)).findById("PAY-001");
    }

    @Test
    void shouldGenerateReceiptForSuccessfulPayment() {

        PaymentRepository repository = mock(PaymentRepository.class);

        Payment payment = new Payment(
                "RID-001",
                "FARE-001",
                785.0,
                "CARD",
                "SUCCESS",
                null
        );

        when(repository.findById("PAY-001"))
                .thenReturn(java.util.Optional.of(payment));

        PaymentService service = new PaymentService(repository);

        ReceiptResponse receipt =
                service.generateReceipt("PAY-001");

        assertEquals("RID-001", receipt.getRideId());
        assertEquals(785.0, receipt.getAmount());
        assertEquals("CARD", receipt.getPaymentMethod());
        assertEquals("SUCCESS", receipt.getStatus());
        assertEquals(
                "Payment completed successfully",
                receipt.getReceiptMessage()
        );

        verify(repository, times(1)).findById("PAY-001");
    }
}
