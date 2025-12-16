package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.model.customer.CartProxy;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.customer.CustomerService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.exception.CartException;
import com.pattern.food_ordering_system.utils.exception.InvalidQuantityException;
import com.pattern.food_ordering_system.utils.exception.RestaurantMismatchException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.Locale;

public class FoodCardController {

    @FXML
    private ImageView imgFood;
    @FXML
    private Label lblName;
    @FXML
    private Label lblRestaurant;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblRatingText;
    @FXML
    private Label lblPrice;
    private FoodItem foodItem;
    private static CustomerController parentController;

    public void setData(FoodItem item) {
        this.foodItem = item;
        lblName.setText(item.getName());
        lblRestaurant.setText("By " + item.getRestaurantName());
        lblDescription.setText(item.getDescription());
        lblPrice.setText(String.format(Locale.US, "%.2f EGP", item.getPrice()));

        int rating = (int) item.getRating();
        String stars = "⭐".repeat(rating);

        if (rating == 0) {
            stars = "Not Rated";
        }

        lblRatingText.setText(stars);


        try {
            if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                imgFood.setImage(new Image(item.getImagePath(), true));
            } else {
                imgFood.setImage(new Image("https://assets.bonappetit.com/photos/63a390eda38261d1c3bdc555/16:9/w_1920,c_limit/best-food-writing-2022-lede.jpg", true));
            }
        } catch (Exception e) {
            imgFood.setImage(new Image("https://assets.bonappetit.com/photos/63a390eda38261d1c3bdc555/16:9/w_1920,c_limit/best-food-writing-2022-lede.jpg", true));
        }

    }

    public void addFoodItemToCart() {
        try {
            CustomerService.updateCartItem(foodItem, 1, 0);
            parentController.loadCartMenu();
        } catch (RestaurantMismatchException e) {
            boolean confirm = AlertHandler.confirm("Clear Cart", e.getMessage()); // Clear cart & add new food item from different restaurant
            if (confirm) {
                CustomerService.clearCart();
                addFoodItemToCart();
            }
        } catch (InvalidQuantityException e) {
            AlertHandler.showWarning("Failed Operation", e.getMessage());
        } catch (CartException e) {
            AlertHandler.showError("Internal Server Error", e.getMessage());
        }
    }


    public static void setParentController(CustomerController parentController) {
        FoodCardController.parentController = parentController;
    }
}