package com.pattern.food_ordering_system.utils;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*== DP >> Flyweight Pattern ==*/
public class ViewHandler {
    private static Scene mainScene;
    private static Map<String, ViewCacheEntry> cache = new HashMap<>();
    private static String previousFXMLFile = "";

    public static void changeView(Stage stage, String fxmlFile) {

        try {
            ViewCacheEntry entry;
            boolean isOld = false;
            if (cache.containsKey(fxmlFile)) {
                entry = cache.get(fxmlFile);
                isOld = true;
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(ViewHandler.class.getResource("/fxml-views/" + fxmlFile + ".fxml"));
                entry = new ViewCacheEntry(fxmlLoader.load(), fxmlLoader.getController());
                cache.put(fxmlFile, entry);
            }

            if (mainScene == null) {
                mainScene = new Scene(entry.root, 800, 500);
                stage.setScene(mainScene);
            } else {
                mainScene.setRoot(entry.root);
            }

            if (isOld && entry.controller instanceof Initializable && notSameDashboard(previousFXMLFile, fxmlFile)) {
                ((Initializable) entry.controller).initialize(null, null);
            }
            previousFXMLFile = fxmlFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean notSameDashboard(String currentFxml, String nextFxml) {
        return !((currentFxml.startsWith("restaurant") && nextFxml.startsWith("restaurant")) ||
                (currentFxml.startsWith("customer") && nextFxml.startsWith("customer")) ||
                (currentFxml.startsWith("delivery") && nextFxml.startsWith("delivery")));
    }

    private static class ViewCacheEntry {
        Parent root;
        Object controller;

        ViewCacheEntry(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }
    }
}




