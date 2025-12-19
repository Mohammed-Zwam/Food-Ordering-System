package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.OrderItem;
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
    @FXML
    private Button rateBtn;
    private RestaurantOrder order;

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
    }


    public void setOrderStatus() {
        order.getOrderStatus();
    }
}
