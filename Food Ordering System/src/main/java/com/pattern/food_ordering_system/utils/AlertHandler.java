package com.pattern.food_ordering_system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertHandler {
    private static Alert error = new Alert(Alert.AlertType.ERROR);
    private static Alert warning = new Alert(Alert.AlertType.WARNING);
    private static Alert info = new Alert(Alert.AlertType.INFORMATION);
    private static Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);

    public static void showError(String errorTitle, String errorMessage) {
        error.setTitle(errorTitle);
        error.setHeaderText(null);
        error.setContentText(errorMessage);
        error.showAndWait();
    }

    public static void showWarning(String warningTitle,String warningMessage) {
        warning.setTitle(warningTitle);
        warning.setHeaderText(null);
        warning.setContentText(warningMessage);
        warning.showAndWait();
    }

    public static void showInfo(String infoTitle, String warningMessage) {
        info.setTitle(infoTitle);
        info.setHeaderText(null);
        info.setContentText(warningMessage);
        info.showAndWait();
    }

    public static boolean confirm(String confirmTitle, String confirmMessage) {
        confirm.setTitle(confirmTitle);
        confirm.setHeaderText(null);
        confirm.setContentText(confirmMessage);
        return confirm.showAndWait().get() == ButtonType.OK;
    }
}
