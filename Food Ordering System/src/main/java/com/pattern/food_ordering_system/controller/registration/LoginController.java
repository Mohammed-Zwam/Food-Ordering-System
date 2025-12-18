package com.pattern.food_ordering_system.controller.registration;

import com.pattern.food_ordering_system.service.user.UserService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.ViewHandler;
import com.pattern.food_ordering_system.validatorMW.InputValidator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private void handleLogin(MouseEvent event) {
        isLoading(true);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        /*== INCREASE PERFORMANCE BY USE ANOTHER THREAD FOR BUSINESS LOGIC ;) ==*/

        Task<String> loginTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                InputValidator.isEmptyOrNull(userName);
                InputValidator.isEmptyOrNull(password);
                return UserService.login(userName.getText(), password.getText());
            }
        };

        loginTask.setOnSucceeded(e -> {
            ViewHandler.changeView(stage, loginTask.getValue());
            isLoading(false);
        });

        loginTask.setOnFailed(e -> {
            Throwable ex = loginTask.getException();
            if (ex instanceof RuntimeException) {
                AlertHandler.showWarning("Invalid Input", ex.getMessage());
                ex.printStackTrace();
            } else
                AlertHandler.showError("Login Failed", ex.getMessage());
            isLoading(false);
        });

        new Thread(loginTask).start();
    }

    @FXML
    private void handleSignUpNavigation(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewHandler.changeView(stage, "registration-views/signup-view");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isLoading(boolean isLoading) {
        if (isLoading) {
            loginBtn.setText("⌛ Loading ...");
        } else {
            loginBtn.setText("Login");
        }
        loginBtn.setDisable(isLoading);
    }

}
