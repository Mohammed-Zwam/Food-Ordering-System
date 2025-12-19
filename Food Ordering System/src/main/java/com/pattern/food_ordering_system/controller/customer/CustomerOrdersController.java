package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.Order;
import com.pattern.food_ordering_system.model.customer.CustomerOrder;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.customer.CustomerService;
import com.pattern.food_ordering_system.service.user.UserService;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerOrdersController implements Initializable {

    Customer customer = (Customer) UserFactory.getUser();
    @FXML
    private VBox emptyOrdersMessage, ordersContainer;
    @FXML
    private ScrollPane ordersMainContainer;

    @FXML
    private Label userName;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button refreshBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task<List<CustomerOrder>> task = new Task<>() {
            @Override
            protected List<CustomerOrder> call() throws Exception {
                return customer.getOrders();
            }
        };

        task.setOnSucceeded(event -> {
            List<CustomerOrder> orders = task.getValue();
            renderOrders(orders);
        });
        isEmptyPage(customer.getOrders().isEmpty());
        setCustomerInfo();
        new Thread(task).start();
    }

    private void setCustomerInfo() {
        userName.setText(customer.getUserName());
        if (!(customer.getUserImgPath() == null || customer.getUserImgPath().equalsIgnoreCase("default"))) {
            Image image = new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(customer.getUserImgPath()))
            );
            profileImage.setImage(image);
        }
    }

    private void renderOrders(List<CustomerOrder> orders) {
        ordersContainer.getChildren().clear();
        for (CustomerOrder order : orders) {
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

    private void isEmptyPage(boolean isEmpty) {
        emptyOrdersMessage.setVisible(isEmpty);
        emptyOrdersMessage.setManaged(isEmpty);

        ordersMainContainer.setVisible(!isEmpty);
        ordersMainContainer.setManaged(!isEmpty);
    }

    @FXML
    void refreshMenu() {
        refreshBtn.setDisable(true);
        refreshBtn.setText("⏳ Refreshing ...");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                CustomerService.loadCustomerOrders();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            initialize(null, null);
            refreshBtn.setText("\uD83D\uDD04 Refresh");
            refreshBtn.setDisable(false);
        });
        new Thread(task).start();
    }
}