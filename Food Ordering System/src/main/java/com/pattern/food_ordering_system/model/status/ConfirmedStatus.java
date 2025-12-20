package com.pattern.food_ordering_system.model.status;

public class ConfirmedStatus extends Status {
    public ConfirmedStatus() {
        super.setOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.BEING_PREPARED);
    }
}
