package com.pattern.food_ordering_system.service.payment;

import com.pattern.food_ordering_system.service.payment.external.StripeService;

public class CreditCardAdapter implements PaymentGateway {
    private StripeService stripeService;

    public CreditCardAdapter() {
        this.stripeService = new StripeService();
    }

    @Override
    public boolean processPayment(double amount) {
        try {
            stripeService.payWithCard("1234-5678-9012-3456", amount); //example
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}