package com.pattern.food_ordering_system.model.status;


public class ReadyForDeliveryStatus extends Status {
    public ReadyForDeliveryStatus() {
        super.setOrderStatus(OrderStatus.READY_FOR_DELIVERY);
    }


    @Override
    public void nextStatus(Status status) {
        status = StatusFactory.getOrderStatusObj(OrderStatus.OUT_FOR_DELIVERY);
    }
}
