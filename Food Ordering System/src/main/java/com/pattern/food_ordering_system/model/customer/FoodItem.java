package com.pattern.food_ordering_system.model.customer;

import com.pattern.food_ordering_system.entity.MenuItem;

public class FoodItem extends MenuItem {
    private String restaurantName;
    private long restaurantId;
    private String restaurantLogo;
    private double rating;
    private String category;
    private String location;


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }
}
