package com.ridelink.fare.dto;

public class ReceiptResponse {

    private String paymentId;
    private String rideId;
    private double amount;
    private String paymentMethod;
    private String status;
    private String receiptMessage;

    public ReceiptResponse() {
    }

    public ReceiptResponse(String paymentId,
                           String rideId,
                           double amount,
                           String paymentMethod,
                           String status,
                           String receiptMessage) {
        this.paymentId = paymentId;
        this.rideId = rideId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.receiptMessage = receiptMessage;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiptMessage() {
        return receiptMessage;
    }
}