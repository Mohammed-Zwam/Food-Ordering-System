package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.RestaurantRepo;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.InputParser;
import com.pattern.food_ordering_system.validatorMW.InputValidator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditItemController {

    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextArea description;
    @FXML
    private TextField imgUrl;
    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private ImageView imgPreview;
    @FXML
    private CheckBox chkAvailable;
    private final Restaurant restaurant = (Restaurant) UserFactory.getUser();
    private MenuItem item;
    private RestaurantController parentController;

    @FXML
    public void initialize() {
        cmbCategory.setEditable(true);
    }

    public void setData(MenuItem item, RestaurantController parent) {
        this.item = item;
        this.parentController = parent;

        cmbCategory.setItems(FXCollections.observableArrayList(
                restaurant.getMenu().getAllMenuCategories()
        ));
        cmbCategory.setValue(item.getParent().getName());
        name.setText(item.getName());
        price.setText(String.valueOf(item.getPrice()));
        description.setText(item.getDescription());
        imgUrl.setText(item.getImagePath());
        chkAvailable.setSelected(item.isAvailable());


        loadImageSafely(item.getImagePath());


        imgUrl.textProperty().addListener((obs, oldVal, newVal) -> {
            loadImageSafely(newVal);
        });
    }

    private void loadImageSafely(String url) {
        if (url != null && !url.trim().isEmpty()) {
            try {
                imgPreview.setImage(new Image(url, true));
            } catch (Exception e) {
                imgPreview.setImage(null);
            }
        } else {
            imgPreview.setImage(null);
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        try {
            InputValidator.isEmptyOrNull(name);
            InputValidator.isSelected(cmbCategory);
            InputValidator.isEmptyOrNull(price);
            InputValidator.isValidPrice(InputParser.toDouble(price));
            InputValidator.isEmptyOrNull(imgUrl);
        } catch (RuntimeException error) {
            AlertHandler.showWarning("Invalid Input", error.getMessage());
            return;
        }

        item.setName(InputParser.toString(name));
        item.setPrice(InputParser.toDouble(price));
        item.setDescription(InputParser.toString(description));
        item.setAvailable(chkAvailable.isSelected());
        item.setImagePath(InputParser.toString(imgUrl));

        boolean success = RestaurantRepo.updateFoodItem(cmbCategory.getValue(), item);

        if (success) {
            AlertHandler.showInfo("Operation Completed", "Item updated successfully.");
            parentController.refreshMenu();
            closeWindow(event);
        } else {
            AlertHandler.showError("Operation Failed", "Failed to update item.");
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
