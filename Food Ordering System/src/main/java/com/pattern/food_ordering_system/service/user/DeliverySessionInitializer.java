package com.pattern.food_ordering_system.service.user;


public class DeliverySessionInitializer implements UserSessionInitializer {
    @Override
    public String getTargetScene() {
        return "delivery-views/delivery-view";
    }

    @Override
    public void loadUserData() {

    }
}
