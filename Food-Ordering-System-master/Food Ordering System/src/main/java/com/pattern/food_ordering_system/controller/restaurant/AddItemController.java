package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.restaurant.RestaurantService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.InputParser;
import com.pattern.food_ordering_system.validatorMW.InputValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddItemController {

    @FXML
    private TextField name, price, imgUrl;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private ImageView imgPreview;
    @FXML
    private Button btnSave;
    @FXML
    private CheckBox chkAvailable;
    private RestaurantController parentController;

    private Restaurant restaurant = (Restaurant) UserFactory.getUser();

    @FXML
    public void initialize() {
        cmbCategory.setEditable(true);
        loadCategories();
        imgUrl.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    Image image = new Image(newValue, true);
                    imgPreview.setImage(image);
                } catch (Exception e) {
                    imgPreview.setImage(null);
                }
            } else {
                imgPreview.setImage(null);
            }
        });
    }

    public void loadCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList(restaurant.getMenu().getAllMenuCategories());
        cmbCategory.setItems(categories);
    }

    public void setParentController(RestaurantController parent) {
        this.parentController = parent;
    }


    @FXML
    private void handleSave() {
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

        MenuItem foodItem = new MenuItem();
        foodItem.setName(InputParser.toString(name));
        foodItem.setPrice(InputParser.toDouble(price));
        foodItem.setDescription(InputParser.toString(description));
        foodItem.setImagePath(InputParser.toString(imgUrl));
        foodItem.setAvailable(chkAvailable.isSelected());

        try {
            RestaurantService.addFoodItem(cmbCategory.getValue(), foodItem);
            AlertHandler.showInfo("Success", "Item Added!");
            if (parentController != null) parentController.refreshMenu();
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (RuntimeException error) {
            AlertHandler.showError("Error", error.getMessage());
        }
    }
}