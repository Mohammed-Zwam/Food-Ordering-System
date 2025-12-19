package com.pattern.food_ordering_system.model.status;


import com.pattern.food_ordering_system.model.customer.OrderStatus;

public class OurForDeliveryStatus extends Status {
    public OurForDeliveryStatus() {
        super.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
    }


    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.DELIVERED);
    }
}
