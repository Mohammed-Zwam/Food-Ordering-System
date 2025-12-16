package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.customer.Cart;
import com.pattern.food_ordering_system.entity.Order;

import java.util.List;

public class Customer extends User {
    private Cart cart;
    private List<Order> orders;

    public Customer() {
        cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
