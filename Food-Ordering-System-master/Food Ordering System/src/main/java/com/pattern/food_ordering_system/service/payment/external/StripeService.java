package com.pattern.food_ordering_system.service.payment.external;

public class StripeService {
    public void payWithCard(String cardNumber, double amount) {
        System.out.println("[Stripe API] Processing credit card payment: $" + amount);
    }
}