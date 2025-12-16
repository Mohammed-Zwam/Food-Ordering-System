package com.pattern.food_ordering_system.service.payment;

public interface PaymentGateway {
    boolean processPayment(double amount);
}