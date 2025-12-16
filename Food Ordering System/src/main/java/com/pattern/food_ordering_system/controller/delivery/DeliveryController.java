package com.pattern.food_ordering_system.controller.delivery;

import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class DeliveryController{
    @FXML
    private Label lblWelcome;
    @FXML
    private VBox categoriesContainer;
    @FXML
    private ImageView profileImage;




    public void expandAll() {
        for (Node node : categoriesContainer.getChildren()) {
            TitledPane fp = (TitledPane) node;
            fp.setExpanded(true);
        }
    }

    public void collapseAll() {
        for (Node node : categoriesContainer.getChildren()) {
            TitledPane fp = (TitledPane) node;
            fp.setExpanded(false);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }
}