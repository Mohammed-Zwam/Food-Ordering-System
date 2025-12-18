package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.RestaurantOrder;

import java.util.List;

public class Restaurant extends User {
    List<RestaurantOrder> orders;
    private int AVG_RATE; // from 1 to 5
    private Menu menu;

    public int getAVG_RATE() {
        return AVG_RATE;
    }

    public void setAVG_RATE(int AVG_RATE) {
        this.AVG_RATE = AVG_RATE;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<RestaurantOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<RestaurantOrder> orders) {
        this.orders = orders;
    }
}