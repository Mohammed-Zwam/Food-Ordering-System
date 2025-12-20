package com.pattern.food_ordering_system.model.customer;

import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.entity.Review;


public class CustomerOrder extends Order {
    private String restaurantName;
    private String restaurantLogo;
    private long restaurantId;
    private long customerId;
    private double totalPriceWithFee;
    private Review review;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public double getTotalPriceWithFee() {
        return totalPriceWithFee;
    }

    public void setTotalPriceWithFee(double totalPriceWithFee) {
        this.totalPriceWithFee = totalPriceWithFee;
    }

    public double getTotalPrice() {
        return totalPriceWithFee;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
