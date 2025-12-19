package com.pattern.food_ordering_system.service.user;
import com.pattern.food_ordering_system.service.delivery.DeliveryService;
public class DeliverySessionInitializer implements UserSessionInitializer{
    @Override
    public String getTargetScene() {
        return "delivery-views/delivery-view";
    }

    @Override
    public void loadUserData() {
        DeliveryService.loadPendingOrders();
    }
}
