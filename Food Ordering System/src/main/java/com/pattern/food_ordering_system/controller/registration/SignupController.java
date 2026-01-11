package com.pattern.food_ordering_system.controller.registration;

import com.pattern.food_ordering_system.service.user.UserService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.Locations;
import com.pattern.food_ordering_system.utils.ViewHandler;
import com.pattern.food_ordering_system.validatorMW.InputValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SignupController {

    @FXML
    private RadioButton customerRadio, restaurantRadio, deliveryRadio;

    @FXML
    private Button uploadButton;

    @FXML
    private ImageView profileImage;

    private ToggleGroup accountTypeGroup;

    @FXML
    private ComboBox<String> zoneCombo;

    @FXML
    TextField userName;

    @FXML
    TextField phoneNumber;

    @FXML
    PasswordField password;

    @FXML
    PasswordField confirmPassword;


    @FXML
    public void initialize() {
        accountTypeGroup = new ToggleGroup();
        customerRadio.setToggleGroup(accountTypeGroup);
        restaurantRadio.setToggleGroup(accountTypeGroup);
        deliveryRadio.setToggleGroup(accountTypeGroup);

        // Load Zones
        zoneCombo.getItems().clear();
        zoneCombo.getItems().addAll(Locations.ZONES);

        // Upload Image Event Handler
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Logo/Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
            );
            Stage stage = (Stage) uploadButton.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                profileImage.setImage(new Image(file.toURI().toString()));
            }
        });
    }


    @FXML
    private void handleLoginNavigation(MouseEvent event) {
        try {
            clearForm();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewHandler.changeView(stage, "registration-views/login-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSignup(MouseEvent event) {
        try {
            InputValidator.isEmptyOrNull(userName);
            InputValidator.isEmptyOrNull(phoneNumber);
            InputValidator.isPhoneNumber(phoneNumber);
            InputValidator.isSelected(zoneCombo);
            InputValidator.isWeekPassword(password);
            InputValidator.isPasswordMatching(password, confirmPassword);
            InputValidator.isSelected(accountTypeGroup);
        } catch (RuntimeException error) {
            AlertHandler.showWarning("Invalid Input", error.getMessage());
            return;
        }

        String result = UserService.signup(
                userName.getText(),
                password.getText(),
                zoneCombo.getValue(),
                ((RadioButton) accountTypeGroup.getSelectedToggle()).getText(),
                phoneNumber.getText(),
                profileImage
        );

        AlertHandler.showInfo("Registration Complete", result);
        if (result == "Account Created Successfully") {
            clearForm();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewHandler.changeView(stage, "registration-views/login-view");
        }
    }
    private void clearForm () {
        if (accountTypeGroup != null) {
            accountTypeGroup.selectToggle(null);
        }

        if (zoneCombo != null) {
            zoneCombo.getSelectionModel().clearSelection();
        }

        if (userName != null) userName.clear();
        if (phoneNumber != null) phoneNumber.clear();

        if (password != null) password.clear();
        if (confirmPassword != null) confirmPassword.clear();

        if (profileImage != null) {
            profileImage.setImage(
                    new Image(getClass().getResource("/assets/user-icon-SYS.png").toExternalForm())
            );
        }
    }
}
