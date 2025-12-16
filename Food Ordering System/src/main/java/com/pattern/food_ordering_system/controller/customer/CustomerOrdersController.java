package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerOrdersController implements Initializable {

    Customer customer = (Customer) UserFactory.getUser();
    @FXML
    private VBox ordersContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOrdersData();
    }

    private void loadOrdersData() {
        List<Order> dbOrders = customer.getOrders();
        ordersContainer.getChildren().clear();
        for (Order order : dbOrders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/customer-views/order-card.fxml"));
                VBox card = loader.load();
                OrderCardController cardController = loader.getController();
                cardController.setOrderData(order);
                ordersContainer.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading order card: " + e.getMessage());
            }
        }
    }

    @FXML
    void goHome(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "customer-views/customer-view");
    }

    @FXML
    void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }
}