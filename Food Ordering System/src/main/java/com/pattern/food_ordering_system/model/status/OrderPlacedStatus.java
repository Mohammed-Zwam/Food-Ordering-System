package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.entity.Order;

public class OrderPlacedStatus extends Status{
    public OrderPlacedStatus() {
        super.setOrderStatus(OrderStatus.ORDER_PLACED);
    }

    @Override
    public void nextStatus(Order order) {
        order.setOrderStatus(StatusFactory.getOrderStatusObj(OrderStatus.CONFIRMED));
    }
}
