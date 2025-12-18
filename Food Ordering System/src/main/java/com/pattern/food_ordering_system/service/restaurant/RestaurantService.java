package com.pattern.food_ordering_system.service.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.MenuComponent;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.RestaurantRepo;
import com.pattern.food_ordering_system.utils.AlertHandler;

public class RestaurantService {
    private static final Restaurant restaurant = (Restaurant) UserFactory.getUser();

    public static void setRestaurantInfo() {
        getRestaurantMenu();
        getRestaurantOrders();
        restaurant.setAVG_RATE(RestaurantRepo.findAvgRateByRestaurantId(restaurant.getId()));
    }

    public static void getRestaurantMenu() {
        restaurant.setMenu(RestaurantRepo.getMenuByRestaurantId(restaurant.getId()));
    }

    public static void getRestaurantOrders() {
        restaurant.setOrders(RestaurantRepo.findOrdersByRestaurantId(restaurant.getId()));
    }

    public static void addFoodItem(String category, MenuItem foodItem) {
        long newId = RestaurantRepo.addFoodItem(restaurant.getId(), category, foodItem);
        if (newId == -1) throw new RuntimeException("Failed to save to database.");
        foodItem.setId(newId);
        Menu menu = restaurant.getMenu();
        menu.add(category, foodItem);
    }

    public static void deleteFoodItemById(MenuItem menuItem) throws RuntimeException {
        boolean success = RestaurantRepo.deleteFoodItem(menuItem.getId());
        if (success) {
            AlertHandler.showInfo("Operation Completed", "Item deleted successfully.");
            restaurant.getMenu().remove(menuItem);
        } else {
            throw new RuntimeException("Failed to delete item.");
        }
    }

}
