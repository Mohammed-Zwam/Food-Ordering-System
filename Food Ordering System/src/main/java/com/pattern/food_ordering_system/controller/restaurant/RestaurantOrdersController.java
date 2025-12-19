package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.model.restaurant.RestaurantOrder;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.restaurant.RestaurantService;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantOrdersController implements Initializable {
    Restaurant restaurant = (Restaurant) UserFactory.getUser();
    @FXML
    private Label userName;
    @FXML
    private ImageView profileImage;
    @FXML
    private VBox emptyOrdersMessage, ordersContainer;
    @FXML
    private ScrollPane ordersMainContainer;

    @FXML
    private Button refreshBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!restaurant.getOrders().isEmpty()) {

            Task<List<RestaurantOrder>> task = new Task<>() {
                @Override
                protected List<RestaurantOrder> call() throws Exception {
                    return restaurant.getOrders();
                }
            };

            task.setOnSucceeded(event -> {
                List<RestaurantOrder> orders = task.getValue();
                renderOrders(orders);
            });

            new Thread(task).start();
        }
        isEmptyPage(restaurant.getOrders().isEmpty());
        setRestaurantInfo();
    }

    private void renderOrders(List<RestaurantOrder> orders) {
        ordersContainer.getChildren().clear();
        FlowPane ordersGrid = new FlowPane();
        ordersGrid.setHgap(20);
        ordersGrid.setVgap(20);
        ordersGrid.setPadding(new Insets(10));


        for (RestaurantOrder order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/restaurant-views/order-card.fxml"));
                VBox card = loader.load();
                OrderCardController cardController = loader.getController();
                cardController.setOrderData(order);
                ordersGrid.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading order card: " + e.getMessage());
            }
        }

        ordersContainer.getChildren().add(ordersGrid);
    }

    public void setRestaurantInfo() {
        userName.setText(restaurant.getUserName());
        if (!(restaurant.getUserImgPath().equals("default"))) {
            try {
                Image imageFile = new Image(Objects.requireNonNull(RestaurantController.class.getResourceAsStream(restaurant.getUserImgPath())));
                profileImage.setImage(imageFile);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void goHome(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "restaurant-views/restaurant-view");
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
                RestaurantService.getRestaurantOrders();
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


    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }
}
