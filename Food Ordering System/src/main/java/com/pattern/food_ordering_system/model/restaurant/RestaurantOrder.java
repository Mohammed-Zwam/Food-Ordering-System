package com.pattern.food_ordering_system.model.restaurant;

import com.pattern.food_ordering_system.entity.Order;

public class RestaurantOrder extends Order {
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
