package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.customer.CustomerOrder;
import java.util.List;

public class Delivery extends User {
    private List<CustomerOrder> assignedOrders;

    public List<CustomerOrder> getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(List<CustomerOrder> assignedOrders) {
        this.assignedOrders = assignedOrders;
    }
}