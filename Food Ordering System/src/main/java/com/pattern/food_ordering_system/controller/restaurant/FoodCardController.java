package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.repository.RestaurantRepo;
import com.pattern.food_ordering_system.service.restaurant.RestaurantService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FoodCardController {
    @FXML
    private Label lblName;
    @FXML
    private Text txtDescription;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblStatus;
    @FXML
    private ImageView imgFood;
    private MenuItem item;
    private RestaurantController parentController;

    public void setData(MenuItem item, RestaurantController parentController) {
        this.item = item;
        this.parentController = parentController;

        lblName.setText(item.getName());
        txtDescription.setText(item.getDescription());
        lblPrice.setText(item.getPrice() + " EGP");
        lblStatus.setText(item.isAvailable() ? "Available" : "Not Available");
        if (!item.isAvailable()) {
            lblStatus.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold; -fx-font-size: 10px; -fx-background-color: #FAC3C3;  -fx-border-color: #F08D8D; -fx-border-radius: 50%; -fx-background-radius: 15; -fx-padding: 5 10;");
        }

        String imagePath = item.getImagePath();
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        try {
            if (imagePath.startsWith("http") || imagePath.startsWith("https")) {
                imgFood.setImage(new Image(imagePath, true));
            } else {
                String localPath = "src/main/resources/users-images/" + imagePath;
                File file = new File(localPath);
                if (file.exists()) {
                    imgFood.setImage(new Image(file.toURI().toString()));
                }
            }

        } catch (Exception e) {
            System.out.println("Image load error for " + item.getName());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/restaurant-views/edit-item-dialog.fxml"));
            Parent root = loader.load();

            EditItemController editController = loader.getController();
            editController.setData(item, parentController);

            Stage stage = new Stage();
            stage.setTitle("Edit Item");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDelete(ActionEvent event) {
        boolean isConfirmed = AlertHandler.confirm("Delete Item", "Are you sure you want to delete this item?");
        if (!isConfirmed) {
            return;
        }
        try {
            RestaurantService.deleteFoodItemById(item);
            parentController.refreshMenu();
        } catch (Exception e) {
            AlertHandler.showError("Operation Failed", e.getMessage());
        }
    }
}