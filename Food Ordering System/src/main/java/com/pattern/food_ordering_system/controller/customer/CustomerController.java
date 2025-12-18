package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.MenuComponent;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.InputParser;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    Label totalPrice;
    @FXML
    Label restaurantName;
    @FXML
    private Label userName, cartItemsCounter;
    @FXML
    private ImageView profileImage;
    @FXML
    private FlowPane menuFlowPane, cartFlowPane;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> cmbRating, cmbLocation;

    @FXML
    private VBox cartInfoContainer, emptyCartMessageContainer;

    @FXML
    private Button refreshBtn;

    private Menu menu;
    private List<FoodItem> allItems;

    private final Customer customer = (Customer) UserFactory.getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMenuDataForCustomer();
        setupRatingFilter();
        setupLocationFilter();
        setCustomerInfo();
        loadCartMenu();
        FoodCardController.setParentController(this);
        CartCardController.setParentController(this);
    }

    private void setCustomerInfo() {
        userName.setText(customer.getUserName());
        if (!(customer.getUserImgPath() == null || customer.getUserImgPath().equalsIgnoreCase("default"))) {
            Image image = new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream(customer.getUserImgPath()))
            );
            profileImage.setImage(image);
        }
    }

    private void loadMenuDataForCustomer() {
        menu = CustomerRepo.findAllFoodItems();
        allItems = extractAllItems(menu);
        displayItems(allItems);
    }

    private List<FoodItem> extractAllItems(Menu menu) {
        List<FoodItem> items = new ArrayList<>();

        for (MenuComponent component : menu.getMenuComponents()) {

            if (component instanceof FoodItem) {
                items.add((FoodItem) component);
            } else if (component instanceof Menu) {
                items.addAll(extractAllItems((Menu) component));
            }
        }

        return items;
    }

    private void displayItems(List<FoodItem> items) {
        menuFlowPane.getChildren().clear();
        for (FoodItem item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml-views/customer-views/customer-food-card.fxml")
                );

                HBox card = loader.load();

                FoodCardController controller = loader.getController();
                controller.setData(item);

                menuFlowPane.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSearch() {
        String keyword = InputParser.toString(txtSearch).toLowerCase().trim();

        if (keyword.isEmpty()) {
            displayItems(allItems);
            return;
        }

        List<FoodItem> filtered = new ArrayList<>();

        for (FoodItem item : allItems) {
            boolean match =
                    item.getName().toLowerCase().contains(keyword) ||
                            item.getRestaurantName().toLowerCase().contains(keyword) ||
                            item.getDescription().toLowerCase().contains(keyword);

            if (match) filtered.add(item);
        }

        displayItems(filtered);
    }

    @FXML
    private void logout(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "registration-views/login-view");
    }

    private void setupRatingFilter() {
        String selected = cmbRating.getValue();
        if (selected != null) cmbRating.getItems().clear();

        cmbRating.getItems().addAll(
                "All Ratings",
                "⭐",
                "⭐⭐",
                "⭐⭐⭐",
                "⭐⭐⭐⭐",
                "⭐⭐⭐⭐⭐"
        );
        cmbRating.setValue("All Ratings");
    }

    @FXML
    private void onRatingFilter() {
        String selected = cmbRating.getValue();
        if (selected == null) return;

        if (selected.equals("All Ratings")) {
            displayItems(allItems);
            return;
        }

        int rating = selected.length();

        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : allItems) {
            if ((int) item.getRating() == rating) {
                filtered.add(item);
            }
        }

        displayItems(filtered);
    }

    private void setupLocationFilter() {
        List<String> locations = new ArrayList<>();

        for (FoodItem item : allItems) {
            String loc = item.getLocation();
            if (loc != null && !locations.contains(loc)) {
                locations.add(loc);
            }
        }

        cmbLocation.getItems().clear();
        cmbLocation.getItems().add("All Locations");
        cmbLocation.getItems().addAll(locations);
        cmbLocation.setValue("All Locations");
    }

    @FXML
    private void onLocationFilter() {
        String selected = cmbLocation.getValue();
        if (selected == null) return;

        if (selected.equals("All Locations")) {
            displayItems(allItems);
            return;
        }

        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : allItems) {
            if (item.getLocation().equalsIgnoreCase(selected)) {
                filtered.add(item);
            }
        }

        displayItems(filtered);
    }


    public void refreshMenu() {
        initialize(null, null);
    }

    public void loadCartMenu() {
        isEmptyCart(customer.getCart().isEmpty());

        cartFlowPane.getChildren().clear();

        for (CartItem item : customer.getCart().getCartItems()) {
            if (restaurantName == null) item.getFoodItem().getRestaurantName();
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml-views/customer-views/cart-item-card.fxml")
                );

                HBox card = loader.load();
                CartCardController controller = loader.getController();
                controller.setData(item);
                cartFlowPane.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        this.cartItemsCounter.setText(Integer.toString(customer.getCart().getCartItems().size())); // number of items in cart
        this.totalPrice.setText(customer.getCart().getTotalPrice() + " EGP"); // total price
        this.restaurantName.setText(customer.getCart().getRestaurantName()); // set restaurant name
    }

    public void isEmptyCart(boolean isEmpty) {
        cartInfoContainer.setVisible(!isEmpty);
        cartInfoContainer.setManaged(!isEmpty);
        emptyCartMessageContainer.setVisible(isEmpty);
        emptyCartMessageContainer.setManaged(isEmpty);
    }

    @FXML
    private void onCheckout() {
        if (customer.getCart().isEmpty()) {
            AlertHandler.showWarning("Empty Cart", "Plz add items to your cart before checkout");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml-views/customer-views/checkout-dialog.fxml")
            );

            Parent root = loader.load();
            CheckoutDialogController checkoutController = loader.getController();
            checkoutController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Checkout");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            AlertHandler.showError("Error", "Failed to open checkout");
            e.printStackTrace();
        }
    }

    @FXML
    void navigateToOrdersView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewHandler.changeView(stage, "customer-views/customer-orders-view");
    }

}