package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.customer.Cart;

public class Customer extends User {
    private Cart cart;
    public Customer() {
        cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
