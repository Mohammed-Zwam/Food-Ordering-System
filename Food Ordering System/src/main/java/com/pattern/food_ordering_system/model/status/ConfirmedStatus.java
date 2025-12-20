package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.entity.Order;

public class ConfirmedStatus extends Status {
    public ConfirmedStatus() {
        super.setOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    public void nextStatus(Order order) {
        order.setOrderStatus(StatusFactory.getOrderStatusObj(OrderStatus.BEING_PREPARED));
    }
}
