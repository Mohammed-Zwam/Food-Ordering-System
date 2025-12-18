package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.model.customer.CartItem;
import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.model.customer.OrderStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderCardController {
    @FXML
    private Label lblDate;
    @FXML
    private Label lblRestaurantName;
    @FXML
    private Label lblItemsCount;
    @FXML
    private Label lblTotal;
    @FXML
    private ImageView imgRestaurant;

    @FXML
    private VBox ORDER_PLACED, CONFIRMED, BEING_PREPARED, OUT_FOR_DELIVERY, DELIVERED;

    @FXML
    private Button rateBtn;


    private Order order;

    public void setOrderData(Order order) {
        this.order = order;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd • hh:mm a", Locale.ENGLISH);
        lblDate.setText(order.getOrderTime().format(formatter));
        lblTotal.setText(String.format(Locale.US, "EGP %.2f", order.getTotalPrice()));
        int count = order.getItems() != null ? order.getItems().size() : 0;
        lblItemsCount.setText(count + " items");
        lblRestaurantName.setText(order.getRestaurantName());
        rateBtn.setVisible(order.getStatus().equals(OrderStatus.DELIVERED));

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            if (!(order.getRestaurantLogo() == null || order.getRestaurantLogo().equalsIgnoreCase("default"))) {
                Image image = new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream(order.getRestaurantLogo()))
                );
                imgRestaurant.setImage(image);
            }

            setupItemsTooltip(order.getItems());
            setActiveStatues();
        }
    }


    private void setupItemsTooltip(Collection<CartItem> items) {
        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(200));

        tooltip.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        VBox content = createTooltipContent(items);
        tooltip.setGraphic(content);
        lblItemsCount.setTooltip(tooltip);
        lblItemsCount.setStyle("-fx-cursor: hand; -fx-underline: true; -fx-text-fill: #1E88E5;");
    }

    private VBox createTooltipContent(Collection<CartItem> items) {
        VBox container = new VBox(8);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                "-fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 8;");

        Label header = new Label("Order Details");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #1E88E5;");
        container.getChildren().add(header);

        for (CartItem item : items) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            ImageView img = new ImageView();
            img.setFitWidth(30);
            img.setFitHeight(30);
            try {
                img.setImage(new Image(item.getFoodItem().getImagePath()));
                Rectangle clip = new Rectangle(30, 30);
                clip.setArcWidth(8);
                clip.setArcHeight(8);
                img.setClip(clip);
            } catch (Exception e) {

            }

            Label nameQty = new Label(item.getQuantity() + "x " + item.getFoodItem().getName());
            nameQty.setStyle("-fx-font-size: 11; -fx-text-fill: #333;");
            nameQty.setPrefWidth(120);
            nameQty.setWrapText(true);

            double totalItemPrice = item.getQuantity() * item.getFoodItem().getPrice();
            Label price = new Label(String.format("%.0f EGP", totalItemPrice));
            price.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #4CAF50;");

            row.getChildren().addAll(img, nameQty, price);
            container.getChildren().add(row);
        }

        return container;
    }

    private void setActiveStatues() {
        VBox[] stages = {ORDER_PLACED, CONFIRMED, BEING_PREPARED, OUT_FOR_DELIVERY, DELIVERED};
        int idx = order.getStatus().ordinal();
        for (int i = 0; i <= idx; i++) {
            stages[i].setOpacity(1);
        }
        for (int i = idx + 1; i < 5; i++) {
            stages[i].setOpacity(0.1);
        }
    }

    @FXML
    private void onRate() {
        System.out.println("Re-ordering logic for Order ID: " + order.getOrderId());
    }
}