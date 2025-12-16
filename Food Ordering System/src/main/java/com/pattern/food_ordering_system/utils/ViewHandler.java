package com.pattern.food_ordering_system.utils;
import com.pattern.food_ordering_system.FoodOrderingSystem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHandler {
    private static Scene mainScene;
    public static void changeView(Stage stage, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewHandler.class.getResource("/fxml-views/" + fxmlFile + ".fxml"));
            Parent newRoot = fxmlLoader.load();
            if (mainScene == null) {
                mainScene = new Scene(newRoot, 800,500);
                stage.setScene(mainScene);
            }
            else mainScene.setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
