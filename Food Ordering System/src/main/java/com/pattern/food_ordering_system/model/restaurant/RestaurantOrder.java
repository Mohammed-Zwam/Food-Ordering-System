package com.pattern.food_ordering_system.model.restaurant;

import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.entity.Review;

public class RestaurantOrder extends Order {
    private String customerName;
    private Review review;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
