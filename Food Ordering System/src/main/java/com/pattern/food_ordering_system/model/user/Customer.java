package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.customer.Cart;
import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.model.customer.CustomerOrder;

import java.util.List;

public class Customer extends User {
    private Cart cart;
    private List<CustomerOrder> orders;

    public Customer() {
        cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<CustomerOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }
}
