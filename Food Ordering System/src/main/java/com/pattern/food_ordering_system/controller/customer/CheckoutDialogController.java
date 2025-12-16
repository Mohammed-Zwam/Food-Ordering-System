package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.model.customer.Cart;
import com.pattern.food_ordering_system.model.customer.CartItem;
import com.pattern.food_ordering_system.model.customer.PaymentMethod;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.customer.CustomerService;
import com.pattern.food_ordering_system.service.payment.PaymentFactory;
import com.pattern.food_ordering_system.service.payment.PaymentGateway;
import com.pattern.food_ordering_system.utils.AlertHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CheckoutDialogController {

    @FXML
    private Label lblOrderSummary;

    @FXML
    private TextField txtDeliveryAddress;

    @FXML
    private ComboBox<PaymentMethod> cmbPaymentMethod;

    private Customer customer;
    private CustomerController parentController;

    public void initialize() {
        customer = (Customer) UserFactory.getUser();
        setupPaymentMethods();
        updateOrderSummary();
    }

    private void setupPaymentMethods() {
        cmbPaymentMethod.getItems().addAll(PaymentMethod.values());
        cmbPaymentMethod.setValue(PaymentMethod.CASH);
        cmbPaymentMethod.setConverter(new StringConverter<PaymentMethod>() {
            @Override
            public String toString(PaymentMethod object) {
                return object != null ? object.getDisplayName() : "";
            }

            @Override
            public PaymentMethod fromString(String string) {
                for (PaymentMethod pm : PaymentMethod.values()) {
                    if (pm.getDisplayName().equals(string)) {
                        return pm;
                    }
                }
                return null;
            }
        });
    }

    private void updateOrderSummary() {
        double total = customer.getCart().getTotalPrice();
        lblOrderSummary.setText(String.format(Locale.US, "Total: %.2f EGP", total));
    }

    @FXML
    private void onCheckout() {
        String address = txtDeliveryAddress.getText().trim();
        PaymentMethod method = cmbPaymentMethod.getValue();

        if (address.isEmpty()) {
            AlertHandler.showWarning("Missing Information", "Please enter your delivery address");
            return;
        }

        if (method == null) {
            AlertHandler.showWarning("Missing Information", "Please select a payment method");
            return;
        }
        try {
            PaymentGateway paymentGateway = PaymentFactory.getPaymentGateway(method);
            double totalAmount = customer.getCart().getTotalPrice();
            boolean isPaid = paymentGateway.processPayment(totalAmount);

            if (!isPaid) {
                AlertHandler.showError("Payment Failed", "Transaction could not be processed.");
                return;
            }
            Cart cart = customer.getCart();


            List<CartItem> cartItems = new ArrayList<>();
            for (CartItem item : cart.getCartItems()) {
                cartItems.add(new CartItem(item.getFoodItem(), item.getQuantity()));
            }

            Order order = new Order(
                    customer.getId(),
                    cart.getRestaurantId(),
                    cartItems,
                    totalAmount,
                    method,
                    address,
                    cart.getRestaurantName()
            );

            CustomerService.createOrder(order);
            CustomerService.clearCart();
            parentController.loadCartMenu();
            AlertHandler.showInfo("Success", "Order placed successfully!");
            closeDialog();

        } catch (Exception e) {
            AlertHandler.showError("Checkout Failed", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) txtDeliveryAddress.getScene().getWindow();
        stage.close();
    }

    public void setParentController(CustomerController parentController) {
        this.parentController = parentController;
    }
}
