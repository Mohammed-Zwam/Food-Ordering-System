package com.pattern.food_ordering_system.model.status;

/*== DP >> State Pattern ==*/
public abstract class Status {
    OrderStatus status;

    public OrderStatus getCurrentStatus() {
        return status;
    }

    protected void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    abstract public void nextStatus(Status status);
}
