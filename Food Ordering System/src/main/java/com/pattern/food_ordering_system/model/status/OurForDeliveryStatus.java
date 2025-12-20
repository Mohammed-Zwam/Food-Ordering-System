package com.pattern.food_ordering_system.model.status;


import com.pattern.food_ordering_system.entity.Order;

public class OurForDeliveryStatus extends Status {
    public OurForDeliveryStatus() {
        super.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
    }


    @Override
    public void nextStatus(Order order) {
        order.setOrderStatus(StatusFactory.getOrderStatusObj(OrderStatus.DELIVERED));
    }
}
