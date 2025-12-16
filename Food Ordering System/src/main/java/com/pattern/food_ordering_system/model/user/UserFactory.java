package com.pattern.food_ordering_system.model.user;

import com.pattern.food_ordering_system.entity.User;

import java.util.HashMap;

/*== DP >> Factory Pattern ==*/
public class UserFactory {
    private static HashMap<String, User> users = new HashMap<>();
    private static User currentUser = null;

    static {
        users.put("CUSTOMER", new Customer());
        users.put("RESTAURANT", new Restaurant());
        users.put("DELIVERY", new Delivery());
    }

    private UserFactory () {}

    public static User createUser(String role) {
        currentUser = users.get(role);
        return currentUser;
    }

    public static User getUser() {
        return currentUser;
    }
}
