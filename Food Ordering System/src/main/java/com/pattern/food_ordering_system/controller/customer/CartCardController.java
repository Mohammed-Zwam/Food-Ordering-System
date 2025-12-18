package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.service.customer.CustomerService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.exception.InvalidQuantityException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CartCardController {
    @FXML private ImageView foodImageView;
    @FXML private Label foodNameLabel;
    @FXML private Button removeButton;
    @FXML private Button minusButton;
    @FXML private Label quantityLabel;
    @FXML private Label price;
    @FXML private Button plusButton;
    @FXML private Label availabilityLabel;

    private static CustomerController parentController;
    private CartItem cartItem;


    public void setData(CartItem cartItem) {
        this.cartItem = cartItem;
        FoodItem foodItem = cartItem.getFoodItem();
        foodImageView.setImage(new Image(foodItem.getImagePath(), true));
        foodNameLabel.setText(foodItem.getName());
        quantityLabel.setText(Integer.toString(cartItem.getQuantity()));
        price.setText(cartItem.getSubTotal() + " EGP");
        setAvailability(foodItem.isAvailable());
    }



    public void setAvailability(boolean available) {
        if (available) {
            availabilityLabel.setText("Available");
            availabilityLabel.setStyle("""
            -fx-font-size: 11;
            -fx-text-fill: #2e7d32;
            -fx-background-color: #e8f5e9;
            -fx-padding: 2 6;
            -fx-background-radius: 6;
        """);
        } else {
            availabilityLabel.setText("Unavailable");
            availabilityLabel.setStyle("""
            -fx-font-size: 11;
            -fx-text-fill: #c62828;
            -fx-background-color: #ffebee;
            -fx-padding: 2 6;
            -fx-background-radius: 6;
        """);


            plusButton.setDisable(true);
            minusButton.setDisable(true);
            price.setText("");
            quantityLabel.setText("--");
        }
    }


    public void increaseQuantity() {
        handleCartItemActions(1);
    }

    public void decreaseQuantity() {
        handleCartItemActions(-1);
    }

    public void removeCartItem() {
        handleCartItemActions(0);
    }

    private void handleCartItemActions(int change) {
        try {
            CustomerService.updateCartItem(cartItem.getFoodItem(), change, cartItem.getQuantity());
            parentController.loadCartMenu();
        } catch (InvalidQuantityException e) {
            AlertHandler.showWarning("Failed Operation", e.getMessage());
        }
    }

    public static void setParentController(CustomerController parent) {
        CartCardController.parentController = parent;
    }

}

