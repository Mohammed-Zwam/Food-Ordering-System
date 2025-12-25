package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.entity.Order;

public class BeingPreparedStatus extends Status {
    public BeingPreparedStatus() {
        super.setOrderStatus(OrderStatus.BEING_PREPARED);
    }

    @Override
    public void nextStatus(Order order) {
        Status orderStatus = StatusFactory.getOrderStatusObj(OrderStatus.READY_FOR_DELIVERY);
        order.setOrderStatus(orderStatus);
    }
}
