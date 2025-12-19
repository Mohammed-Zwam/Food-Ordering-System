package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.model.customer.OrderStatus;

public class BeingPreparedStatus extends Status{
    public BeingPreparedStatus() {
        super.setOrderStatus(OrderStatus.BEING_PREPARED);
    }
    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.OUT_FOR_DELIVERY);
    }
}
