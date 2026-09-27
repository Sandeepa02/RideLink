package com.ridelink.fare.dto;

public class PaymentResponse {

    private String paymentId;
    private String rideId;
    private String fareId;
    private double amount;
    private String paymentMethod;
    private String status;

    public PaymentResponse() {
    }

    public PaymentResponse(String paymentId,
                           String rideId,
                           String fareId,
                           double amount,
                           String paymentMethod,
                           String status) {
        this.paymentId = paymentId;
        this.rideId = rideId;
        this.fareId = fareId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getRideId() {
        return rideId;
    }

    public String getFareId() {
        return fareId;
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
}