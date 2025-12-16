package com.pattern.food_ordering_system.service.user;

import com.pattern.food_ordering_system.model.user.Role;

public class SessionInitializerFactory {
    public static UserSessionInitializer getUserSession(Role role) {
        return switch (role) {
            case Role.RESTAURANT -> new RestaurantSessionInitializer();
            case  Role.CUSTOMER -> new CustomerSessionInitializer();
            case Role.DELIVERY -> new DeliverySessionInitializer();
            default -> throw new IllegalArgumentException("Unknown role");
        };
    }
}
