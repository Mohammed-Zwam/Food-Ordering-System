package com.pattern.food_ordering_system;

import com.pattern.food_ordering_system.model.customer.Cart;
import com.pattern.food_ordering_system.model.customer.CartItem;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.application.Application;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class FoodOrderingSystem extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        // LOAD MAIN ASSETS (fonts / icon)
        Font.loadFont(getClass().getResourceAsStream("/fonts/Pacifico-Regular.ttf"), 14);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icon.png")));

        // GET STARTING (Login Page)
        ViewHandler.changeView(stage, "registration-views/login-view");
        stage.setTitle("Talabat");
        stage.show();
    }
}