package com.pattern.food_ordering_system.service.user;

public interface UserSessionInitializer {
    String getTargetScene();
    void loadUserData();
}
