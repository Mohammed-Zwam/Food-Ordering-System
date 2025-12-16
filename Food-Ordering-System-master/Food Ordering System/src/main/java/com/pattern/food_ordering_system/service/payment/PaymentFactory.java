package com.pattern.food_ordering_system.service.payment;

import com.pattern.food_ordering_system.model.order.PaymentMethod;

public class PaymentFactory {

    public static PaymentGateway getPaymentGateway(PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                return new CreditCardAdapter();

            case DIGITAL_WALLET:
                return new WalletAdapter();

            case CASH:
                return new CashAdapter();

            default:
                throw new IllegalArgumentException("Unsupported payment method");
        }
    }
}