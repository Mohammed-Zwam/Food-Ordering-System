package com.pattern.food_ordering_system.entity;

public class Review {
    private double rating;
    private String comment;

    public Review(double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}