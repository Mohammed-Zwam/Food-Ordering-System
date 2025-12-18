package com.pattern.food_ordering_system.model.customer;

import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.utils.exception.InvalidQuantityException;
import com.pattern.food_ordering_system.utils.exception.RestaurantMismatchException;

public class CartProxy implements ICart {
    Cart cart;

    public CartProxy(Cart cart) {
        this.cart = cart;
    }

    @Override
    public int updateCartItem(FoodItem foodItem, int change) {
        if (cart.isEmpty() || foodItem.getRestaurantId() == cart.getRestaurantId()) {
            CartItem cartItem = cart.getCartItemById(foodItem.getId());
            if (cartItem == null || (cartItem.getQuantity() + change >= 0 && cartItem.getQuantity() + change <= 5)) {
                return cart.updateCartItem(foodItem, change);
            } else {
                throw new InvalidQuantityException();
            }
        } else throw new RestaurantMismatchException();
    }

}
