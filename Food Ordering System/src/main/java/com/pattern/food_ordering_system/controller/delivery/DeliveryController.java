package com.pattern.food_ordering_system.controller.delivery;

import com.pattern.food_ordering_system.model.delivery.DeliveryOrder;
import com.pattern.food_ordering_system.model.user.Delivery;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.delivery.DeliveryService;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DeliveryController implements Initializable {
    private final Delivery driver = (Delivery) UserFactory.getUser();

    @FXML private Label lblWelcome;
    @FXML private VBox categoriesContainer;
    @FXML private ImageView profileImage;
    @FXML private Button refreshBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDriverInfo();
        loadOrdersTask();
    }

    private void loadOrdersTask() {
        if (refreshBtn != null) refreshBtn.setDisable(true);

        Task<List<DeliveryOrder>> task = new Task<>() {
            @Override
            protected List<DeliveryOrder> call() throws Exception {
                return DeliveryService.getPendingOrders();
            }
        };

        task.setOnSucceeded(event -> {
            renderOrders(task.getValue());
            if (refreshBtn != null) refreshBtn.setDisable(false);
        });

        task.setOnFailed(event -> {
            if (refreshBtn != null) refreshBtn.setDisable(false);
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void renderOrders(List<DeliveryOrder> orders) {
        categoriesContainer.getChildren().clear();

        if (orders == null || orders.isEmpty()) {
            Label emptyLabel = new Label("No orders available right now.");
            emptyLabel.setStyle("-fx-text-fill: #778DA9; -fx-font-size: 16px;");
            categoriesContainer.getChildren().add(emptyLabel);
            return;
        }

        for (DeliveryOrder order : orders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/delivery-views/delivery-order-card.fxml"));
                VBox card = loader.load();

                DeliveryOrderCardController cardController = loader.getController();
                cardController.setOrderData(order, this);

                categoriesContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDriverInfo() {
        lblWelcome.setText("Welcome, " + driver.getUserName());
        String imgPath = driver.getUserImgPath();
        if (imgPath != null
                && !imgPath.equalsIgnoreCase("default")
                && !imgPath.equalsIgnoreCase("null")) {

            File file = new File(imgPath);

            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                profileImage.setImage(image);
            }
        }
    }

    @FXML
    public void refreshOrders() {
        loadOrdersTask();
    }

    public void expandAll() {
        for (Node node : categoriesContainer.getChildren()) {
            if (node instanceof TitledPane) ((TitledPane) node).setExpanded(true);
        }
    }

    public void collapseAll() {
        for (Node node : categoriesContainer.getChildren()) {
            if (node instanceof TitledPane) ((TitledPane) node).setExpanded(false);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }
}