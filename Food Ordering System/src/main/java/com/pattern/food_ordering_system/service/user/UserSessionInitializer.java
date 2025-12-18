package com.pattern.food_ordering_system.service.user;

/*== DP >> Strategy Pattern ==*/
public interface UserSessionInitializer {
    String getTargetScene();
    void loadUserData();
}
