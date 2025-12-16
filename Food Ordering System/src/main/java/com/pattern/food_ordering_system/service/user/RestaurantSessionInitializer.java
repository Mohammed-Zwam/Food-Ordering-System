package com.pattern.food_ordering_system.service.user;

import com.pattern.food_ordering_system.service.restaurant.RestaurantService;

public class RestaurantSessionInitializer implements UserSessionInitializer{
    @Override
    public String getTargetScene() {
        return "restaurant-views/restaurant-view";
    }

    @Override
    public void loadUserData() {
        RestaurantService.setRestaurantInfo();
    }
}
