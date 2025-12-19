package com.pattern.food_ordering_system.controller.delivery;

import com.pattern.food_ordering_system.model.customer.CustomerOrder;
import com.pattern.food_ordering_system.model.customer.OrderStatus;
import com.pattern.food_ordering_system.service.delivery.DeliveryService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DeliveryOrderCardController {
    @FXML private Label lblOrderId, lblRestaurantName, lblCustomerName, lblAddress, lblDeliveryFee, lblPrice, lblPaymentMethod;
    @FXML private Button btnAction;

    private CustomerOrder order;
    private DeliveryController parent;

    public void setOrderData(CustomerOrder order, DeliveryController parent) {
        this.order = order;
        this.parent = parent;

        lblOrderId.setText("Order #" + order.getOrderId());
        lblRestaurantName.setText("🏢 " + order.getRestaurantName());
        lblCustomerName.setText("👤 " + order.getCustomerName());
        lblAddress.setText("📍 " + order.getDeliveryAddress());
        lblPrice.setText(String.format("%.2f EGP", order.getTotalPriceWithFee()));
        lblDeliveryFee.setText(String.format("🛵 Fee: %.2f", order.getDeliveryFee()));

        if (order.getPaymentMethod() != null)
            lblPaymentMethod.setText(order.getPaymentMethod().getDisplayName());
        else
            lblPaymentMethod.setText("Cash");

        updateButtonState();
    }

    private void updateButtonState() {
        if (order.getStatus() == OrderStatus.BEING_PREPARED) {
            btnAction.setText("📦 Pickup Order");
            btnAction.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;");
            btnAction.setOnAction(e -> handlePickup());
        } else if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            btnAction.setText("✅ Mark Delivered");
            btnAction.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand;");
            btnAction.setOnAction(e -> handleComplete());
        }
    }

    private void handlePickup() {
        try {
            DeliveryService.pickupOrder(order.getOrderId());
            parent.refreshOrders();
            AlertHandler.showInfo("Success", "You picked up the order from " + order.getRestaurantName());
        } catch (RuntimeException e) {
            AlertHandler.showWarning("Wait", e.getMessage());
        }
    }

    private void handleComplete() {
        if (AlertHandler.confirm("Confirm", "Did you deliver the order to " + order.getCustomerName() + "?")) {
            DeliveryService.markAsDelivered(order.getOrderId());
            parent.refreshOrders();
        }
    }
}