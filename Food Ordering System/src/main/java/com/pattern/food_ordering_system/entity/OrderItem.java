package com.pattern.food_ordering_system.entity;

public class OrderItem {
    private long orderItemId;
    private long foodItemId;
    private int quantity;
    private String foodItemName;
    private double price;


    public OrderItem(long orderItemId, String foodItemName, long foodItemId, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.foodItemName = foodItemName;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }


    public long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }
}
