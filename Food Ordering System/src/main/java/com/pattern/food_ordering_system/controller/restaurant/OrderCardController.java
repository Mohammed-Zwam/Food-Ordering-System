package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import com.pattern.food_ordering_system.model.restaurant.RestaurantOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrderCardController {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd • hh:mm a", Locale.ENGLISH);
    @FXML
    private Label orderId, customerName, address, paymentMethod, time, price;
    @FXML
    private VBox itemsContainer;

    private RestaurantOrder order;

    @FXML
    private Button btnAccept, btnMarkReady, btnOutForDelivery;

    @FXML
    private Label orderPlaced, confirmed, beingPrepared, completed;


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
        OrderStatus status = order.getOrderStatus().getCurrentStatus();
        switch (status) {
            case OrderStatus.ORDER_PLACED -> {
                btnAccept.setManaged(true);
                btnAccept.setVisible(true);
                orderPlaced.setManaged(true);
                orderPlaced.setVisible(true);
            }
            case OrderStatus.CONFIRMED -> {
                btnMarkReady.setManaged(true);
                btnMarkReady.setVisible(true);
                confirmed.setManaged(true);
                confirmed.setVisible(true);
            }

            case OrderStatus.BEING_PREPARED -> {
                btnOutForDelivery.setManaged(true);
                btnOutForDelivery.setVisible(true);
                beingPrepared.setManaged(true);
                beingPrepared.setVisible(true);
            }

            default -> {
                completed.setManaged(true);
                completed.setVisible(true);
            }
        }
    }
}
