package com.pattern.food_ordering_system.model.status;

import com.pattern.food_ordering_system.entity.Order;

/*== DP >> State Pattern ==*/
public abstract class Status {
    OrderStatus status;

    public OrderStatus getCurrentStatus() {
        return status;
    }

    protected void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    abstract public void nextStatus(Order order);
}
