package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.model.customer.OrderStatus;

public class DeliveredStatus extends Status {
    public DeliveredStatus() {
        super.setOrderStatus(OrderStatus.DELIVERED);
    }


    @Override
    public void nextStatus(Status status) { /* LAST STAGE */ }
}
