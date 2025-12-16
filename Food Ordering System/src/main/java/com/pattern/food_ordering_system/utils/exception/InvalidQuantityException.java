package com.pattern.food_ordering_system.utils.exception;

public class InvalidQuantityException extends CartException {

    public InvalidQuantityException() {
        super("Quantity must be between 1 and 5.\nFor larger orders, please contact our hotline .");
    }
}
