package com.pattern.food_ordering_system.entity;

import com.pattern.food_ordering_system.model.customer.OrderStatus;
import com.pattern.food_ordering_system.model.customer.PaymentMethod;
import com.pattern.food_ordering_system.model.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private long orderId;
    private List<OrderItem> items;
    private double orderPrice;
    private PaymentMethod paymentMethod;
    private String deliveryAddress;
    private LocalDateTime orderTime;
    private OrderStatus status;

    private Status orderStatus;


    public long getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }


    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void nextOrderStatus() {
        orderStatus.nextStatus(this.orderStatus);
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }
}
