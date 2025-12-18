package com.pattern.food_ordering_system.controller.restaurant;

import com.pattern.food_ordering_system.entity.MenuItem;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.MenuComponent;
import com.pattern.food_ordering_system.model.user.Restaurant;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.service.restaurant.RestaurantService;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


public class RestaurantController implements Initializable {
    @FXML
    private Label lblWelcome;
    @FXML
    private VBox categoriesContainer;

    @FXML
    private VBox emptyMenuMessage;


    @FXML
    private ImageView profileImage;
    private Restaurant restaurant = (Restaurant) UserFactory.getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMenuData();
        setRestaurantInfo();
    }

    public void setRestaurantInfo() {
        lblWelcome.setText(restaurant.getUserName());
        if (!(restaurant.getUserImgPath().equals("default"))) {
            try {
                Image imageFile = new Image(Objects.requireNonNull(RestaurantController.class.getResourceAsStream(restaurant.getUserImgPath())));
                profileImage.setImage(imageFile);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void refreshMenu() {
        RestaurantService.setRestaurantInfo();
        initialize(null, null);
    }

    public void loadMenuData() {
        categoriesContainer.getChildren().clear();
        changeMenuView(restaurant.getMenu().getMenuComponents().isEmpty());

        for (MenuComponent subMenu : restaurant.getMenu().getMenuComponents()) {
            String categoryName = subMenu.getName();
            ArrayList<MenuComponent> itemsInCategory = ((Menu) subMenu).getMenuComponents();
            FlowPane categoryContent = new FlowPane();
            categoryContent.setHgap(20);
            categoryContent.setVgap(20);
            categoryContent.setPadding(new Insets(10));
            categoryContent.setPrefWrapLength(800);

            for (MenuComponent item : itemsInCategory) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/restaurant-views/food-card.fxml"));
                    VBox cardBox = loader.load();
                    FoodCardController cardController = loader.getController();
                    cardController.setData((MenuItem) item, this);
                    categoryContent.getChildren().add(cardBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TitledPane section = new TitledPane();

            Label nameLabel = new Label(categoryName);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #444;");

            Label countLabel = new Label(String.valueOf(itemsInCategory.size()));
            countLabel.setStyle(
                    "-fx-background-color: #3498db;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 2 8; " +
                            "-fx-font-size: 12px;"
            );

            HBox header = new HBox(10, nameLabel, countLabel);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(new Insets(0, 10, 0, 10));

            section.setGraphic(header);


            section.setContent(categoryContent);
            section.setExpanded(true);
            section.setAnimated(false);
            section.setStyle("-fx-font-size: 16px; -fx-text-fill: #555; -fx-font-weight: bold; -fx-background-color: #E9EEF6;");

            categoriesContainer.getChildren().add(section);
        }
    }

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
    private void openAddDialog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml-views/restaurant-views/add-item-dialog.fxml"));
            Parent root = loader.load();

            AddItemController addItemController = loader.getController();
            addItemController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New Item");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }


    private void changeMenuView(boolean isEmpty) {
        emptyMenuMessage.setVisible(isEmpty);
        emptyMenuMessage.setManaged(isEmpty);

        categoriesContainer.setVisible(!isEmpty);
        categoriesContainer.setManaged(!isEmpty);
    }
}