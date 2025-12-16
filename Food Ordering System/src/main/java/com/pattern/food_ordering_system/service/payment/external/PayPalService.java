package com.pattern.food_ordering_system.service.payment.external;

public class PayPalService {
    public void sendPayment(String email, double amount) {
        System.out.println("[PayPal API] Sending payment from " + email + ": " + amount);
    }
}