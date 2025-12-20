package com.pattern.food_ordering_system.model.status;


import com.pattern.food_ordering_system.entity.Order;

public class ReadyForDeliveryStatus extends Status {
    public ReadyForDeliveryStatus() {
        super.setOrderStatus(OrderStatus.READY_FOR_DELIVERY);
    }


    @Override
    public void nextStatus(Order order) {
        order.setOrderStatus(StatusFactory.getOrderStatusObj(OrderStatus.OUT_FOR_DELIVERY));
    }
}
