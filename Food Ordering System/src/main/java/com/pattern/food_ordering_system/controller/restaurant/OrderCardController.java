package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.entity.Review;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import com.pattern.food_ordering_system.model.restaurant.RestaurantOrder;
import com.pattern.food_ordering_system.repository.OrderRepo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrderCardController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd • hh:mm a", Locale.ENGLISH);
    @FXML
    private Label orderId, customerName, address, paymentMethod, time, price, statusLabel;
    @FXML
    private VBox itemsContainer;

    @FXML
    private HBox buttonsContainer;

    private RestaurantOrder order;

    @FXML
    private Button changeStatusBtn;


    public void setOrderData(RestaurantOrder order) {
        this.order = order;
        orderId.setText("Order #" + order.getOrderId());
        customerName.setText("Customer: " + order.getCustomerName());
        address.setText("Address: " + order.getDeliveryAddress());
        paymentMethod.setText("Payment: " + order.getPaymentMethod().toString());
        time.setText("Date: " + order.getOrderTime().format(formatter));
        price.setText("Price: " + String.format(Locale.US, "EGP %.2f", order.getOrderPrice()));
        time.setText(order.getOrderTime().format(formatter));
        itemsContainer.getChildren().clear();
        for (OrderItem orderItem : order.getItems()) {
            Label nameQty = new Label('-' + orderItem.getFoodItemName() + " x" + orderItem.getQuantity());
            nameQty.setStyle("-fx-font-size: 13;");
            itemsContainer.getChildren().add(nameQty);
        }

        setOrderStatus();
    }


    public void setOrderStatus() {
        String acceptButtonStyle = "-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;";
        String markReadyButtonStyle = "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;";
        String outForDeliveryButtonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;";
        String viewCustomerRateButtonStyle = "-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 5 12; -fx-cursor: hand;";
        String orderPlacedLabelStyle = "-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #FFF9C4; -fx-border-radius: 20; -fx-background-radius: 15; -fx-padding: 5 10;";
        String confirmedLabelStyle = "-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #FFCC80; -fx-border-radius: 20; -fx-background-radius: 15; -fx-padding: 5 10;";
        String beingPreparedLabelStyle = "-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #E3F2FD; -fx-border-radius: 20; -fx-background-radius: 15; -fx-padding: 5 10;";
        String completedLabelStyle = "-fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-color: #E8F5E9; -fx-border-color: #B3FCC0; -fx-border-radius: 20; -fx-background-radius: 15; -fx-padding: 5 10;";


        OrderStatus status = order.getOrderStatus().getCurrentStatus();
        switch (status) {
            case OrderStatus.ORDER_PLACED -> {
                statusLabel.setStyle(orderPlacedLabelStyle);
                changeStatusBtn.setStyle(acceptButtonStyle);
            }
            case OrderStatus.CONFIRMED -> {
                statusLabel.setStyle(confirmedLabelStyle);
                changeStatusBtn.setStyle(markReadyButtonStyle);
                changeStatusBtn.setText("Being Prepared");
                statusLabel.setText("Confirmed");
            }

            case OrderStatus.BEING_PREPARED -> {
                statusLabel.setStyle(beingPreparedLabelStyle);
                changeStatusBtn.setStyle(outForDeliveryButtonStyle);
                changeStatusBtn.setText("Ready For Delivery");
                statusLabel.setText("Being Prepared");
            }

            default -> {
                statusLabel.setStyle(completedLabelStyle);
                statusLabel.setText("Completed");
                if (order.getReview() == null) {
                    changeStatusBtn.setManaged(true);
                    changeStatusBtn.setVisible(true);
                    buttonsContainer.setManaged(false);
                    buttonsContainer.setVisible(false);
                } else {
                    changeStatusBtn.setText("View Customer Review");
                    changeStatusBtn.setStyle(viewCustomerRateButtonStyle);
                    changeStatusBtn.setOnAction(e -> showReadOnlyReview(order.getReview()));
                }

            }
        }
    }

    private void showReadOnlyReview(Review review) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Customer Review");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(10);
        Rating ratingControl = new Rating();
        ratingControl.setMax(5);
        ratingControl.setRating(review.getRating());
        ratingControl.setMouseTransparent(true);
        ratingControl.setFocusTraversable(false);

        TextArea commentArea = new TextArea();
        commentArea.setText(review.getComment());
        commentArea.setEditable(false);
        commentArea.setWrapText(true);

        content.getChildren().addAll(new Label("Customer Rating:"), ratingControl, new Label("Customer Comment:"), commentArea);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }


    @FXML
    private void changeState() {
        order.nextOrderStatus();
        OrderRepo.updateOrderStatus(order.getOrderId(), order.getOrderStatus().getCurrentStatus());
        setOrderData(order);
    }
}
