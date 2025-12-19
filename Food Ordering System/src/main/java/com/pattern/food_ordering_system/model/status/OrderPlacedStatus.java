package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.model.customer.OrderStatus;

public class OrderPlacedStatus extends Status{
    public OrderPlacedStatus() {
        super.setOrderStatus(OrderStatus.ORDER_PLACED);
    }

    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.CONFIRMED);
    }
}
