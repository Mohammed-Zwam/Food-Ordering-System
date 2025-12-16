package com.pattern.food_ordering_system.utils.exception;

public class RestaurantMismatchException extends CartException{

    public RestaurantMismatchException() {
        super("Your cart contains items from another restaurant.\nClear the cart to add this item ?");
    }
}
