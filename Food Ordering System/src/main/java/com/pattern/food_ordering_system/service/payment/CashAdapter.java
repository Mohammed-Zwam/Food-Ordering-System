package com.pattern.food_ordering_system.service.payment;

public class CashAdapter implements PaymentGateway {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[System] Payment will be collected as CASH on delivery.");
        return true;
    }
}