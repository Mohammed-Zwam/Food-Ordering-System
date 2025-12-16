package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;
import com.pattern.food_ordering_system.model.restaurant.Menu;

public class Restaurant extends User {
    private int AVG_RATE; // from 1 to 5
    private Menu menu;
    // LIST OF ORDERS

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
}