package com.pattern.food_ordering_system.model.customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Cart implements ICart {
    private HashMap<Long, CartItem> cartItems = new HashMap<>();
    private double totalPrice = 0;
    private Long restaurantId;
    private String restaurantName;


    @Override
    public int updateCartItem(FoodItem foodItem, int change) {
        if (cartItems.isEmpty()) {
            this.restaurantId = foodItem.getRestaurantId();
            this.restaurantName = foodItem.getRestaurantName();
        }
        if (change == 0) {
            CartItem cartItem = cartItems.get(foodItem.getId());
            updateTotalPrice(cartItem.getSubTotal() * -1);
            cartItems.remove(foodItem.getId());
        } else {
            CartItem cartItem = cartItems.getOrDefault(foodItem.getId(), null);
            if (cartItem != null) {
                updateTotalPrice(cartItem.getSubTotal() * -1); // remove old price
                cartItem.setQuantity(cartItem.getQuantity() + change);
                updateTotalPrice(cartItem.getSubTotal()); // add new price
                change = cartItem.getQuantity();
                if (change == 0) cartItems.remove(foodItem.getId());
            } else {
                cartItem = new CartItem(foodItem, change);
                cartItems.put(foodItem.getId(), cartItem);
                updateTotalPrice(cartItem.getSubTotal());
            }
        }
        return change;
    }


    public void setCartItemQuantity(long id, int quantity) {
        CartItem cartItem = cartItems.getOrDefault(id, null);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
        }
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.put(cartItem.getFoodItem().getId(), cartItem);
        totalPrice += cartItem.getSubTotal();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void updateTotalPrice(double amount) {
        this.totalPrice += amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public CartItem getCartItemById(long id) {
        return cartItems.getOrDefault(id, null);
    }

    public long getRestaurantId() {
        return restaurantId;
    }


    public Collection<CartItem> getCartItems() {
        return cartItems.values();
    }


    public void clear() {
        cartItems.clear();
        this.totalPrice = 0;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setCartRestaurantInfo() {
        if (cartItems.isEmpty()) return;
        CartItem cart = cartItems.values().iterator().next(); // return first cart item
        this.restaurantId = cart.getFoodItem().getRestaurantId();
        this.restaurantName = cart.getFoodItem().getRestaurantName();
    }
}
