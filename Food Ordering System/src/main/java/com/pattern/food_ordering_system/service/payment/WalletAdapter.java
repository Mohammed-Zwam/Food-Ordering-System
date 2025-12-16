package com.pattern.food_ordering_system.service.payment;

import com.pattern.food_ordering_system.service.payment.external.PayPalService;

public class WalletAdapter implements PaymentGateway {
    private PayPalService payPalService;

    public WalletAdapter() {
        this.payPalService = new PayPalService();
    }

    @Override
    public boolean processPayment(double amount) {
        payPalService.sendPayment("user@example.com", amount);
        return true;
    }
}