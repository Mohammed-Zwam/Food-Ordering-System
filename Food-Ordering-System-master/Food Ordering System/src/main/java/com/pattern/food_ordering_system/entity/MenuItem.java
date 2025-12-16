package com.pattern.food_ordering_system.entity;

import com.pattern.food_ordering_system.model.restaurant.MenuComponent;

public class MenuItem implements MenuComponent {
    private long id;
    private String name;
    private double price;
    private String description;
    private String imagePath;
    private boolean isAvailable;
    private MenuComponent parent;

    public MenuItem() {
    }

    public MenuItem(long id, String name, double price, String description, String imagePath, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
    }

    public long getId() {
        return id;
    }


    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuComponent getParent() {
        return parent;
    }

    public void setParent(MenuComponent parent) {
        this.parent = parent;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
