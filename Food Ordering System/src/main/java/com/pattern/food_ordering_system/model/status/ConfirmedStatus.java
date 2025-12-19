package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.model.customer.OrderStatus;

public class ConfirmedStatus extends Status {
    public ConfirmedStatus() {
        super.setOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.BEING_PREPARED);
    }
}
