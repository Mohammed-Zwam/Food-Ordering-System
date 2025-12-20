package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.CartItem;
import com.pattern.food_ordering_system.model.customer.FoodItem;
import com.pattern.food_ordering_system.model.restaurant.Menu;
import com.pattern.food_ordering_system.model.restaurant.MenuComponent;
import com.pattern.food_ordering_system.model.user.Customer;
import com.pattern.food_ordering_system.model.user.UserFactory;
import com.pattern.food_ordering_system.repository.CustomerRepo;
import com.pattern.food_ordering_system.service.customer.CustomerService;
import com.pattern.food_ordering_system.service.customer.DeliveryTimeService;
import com.pattern.food_ordering_system.utils.AlertHandler;
import com.pattern.food_ordering_system.utils.InputParser;
import com.pattern.food_ordering_system.utils.ViewHandler;
import javafx.concurrent.Task;
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
import java.util.*;

public class CustomerController implements Initializable {
    private final Customer customer = (Customer) UserFactory.getUser();
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
    @FXML
    private ComboBox<String> cmbDeliveryTime;

    private Menu menu;
    private List<FoodItem> allItems;
    private final Map<Long, Double> deliveryTimeCache = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMenuDataForCustomer();
        renderItems(allItems);
        FoodCardController.setParentController(this);
        CartCardController.setParentController(this);
        setCustomerInfo();
        loadCartMenu();
        // SET EVENTS HERE TO PREVENT MULTIPLE INVOKE WHEN SETUP ComboBox |  # ASHRAF ;)
        cmbLocation.setOnAction(null);
        cmbRating.setOnAction(null);
        cmbDeliveryTime.setOnAction(null);

        setupRatingFilter();
        setupLocationFilter();
        setupDeliveryTimeFilter();
        calculateDeliveryTimesOnce();

        cmbDeliveryTime.setOnAction(this::onDeliveryTimeFilter);
        cmbLocation.setOnAction(this::onLocationFilter);
        cmbRating.setOnAction(this::onRatingFilter);
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

    private void renderItems(List<FoodItem> items) {
        System.out.println(">>>>>>>>>>>>>>>> :( :::::::::::::::::::::");
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
            renderItems(allItems);
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

        renderItems(filtered);
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
    private void onRatingFilter(ActionEvent event) {
        String selected = cmbRating.getValue();
        if (selected == null) return;

        if (selected.equals("All Ratings")) {
            renderItems(allItems);
            return;
        }

        int rating = selected.length();

        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : allItems) {
            if ((int) item.getRating() == rating) {
                filtered.add(item);
            }
        }

        renderItems(filtered);
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
    private void onLocationFilter(ActionEvent event) {
        String selected = cmbLocation.getValue();
        if (selected == null) return;

        if (selected.equals("All Locations")) {
            renderItems(allItems);
            return;
        }

        List<FoodItem> filtered = new ArrayList<>();
        for (FoodItem item : allItems) {
            if (item.getLocation().equalsIgnoreCase(selected)) {
                filtered.add(item);
            }
        }

        renderItems(filtered);
    }


    @FXML
    void refreshMenu() {
        refreshBtn.setDisable(true);
        refreshBtn.setText("⏳ Refreshing ...");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                CustomerService.loadCustomerCart();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            initialize(null, null);
            refreshBtn.setText("\uD83D\uDD04 Refresh");
            refreshBtn.setDisable(false);
        });
        new Thread(task).start();
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

    private void onDeliveryTimeFilter(ActionEvent event) {
        String selected = cmbDeliveryTime.getValue();
        if (selected == null || selected.equals("All")) {
            renderItems(allItems);
            return;
        }

        double maxTime = switch (selected) {
            case "≤ 10 min" -> 10;
            case "≤ 15 min" -> 15;
            case "≤ 30 min" -> 30;
            case "≤ 45 min" -> 45;
            case "≤ 60 min" -> 60;
            default -> Double.MAX_VALUE;
        };

        List<FoodItem> filtered = new ArrayList<>();

        for (FoodItem item : allItems) {
            double time = deliveryTimeCache.get(item.getId());
            if (selected.equals("> 60 min")) {
                if (time > 60) filtered.add(item);
            } else {
                if (time <= maxTime) filtered.add(item);
            }
        }

        renderItems(filtered);
    }

    private void calculateDeliveryTimesOnce() {
        String customerZone = customer.getZone();

        for (FoodItem item : allItems) {
            String restaurantZone = item.getLocation();

            double time = DeliveryTimeService.getDeliveryTimeInMinutes(
                    customerZone,
                    restaurantZone
            );

            deliveryTimeCache.put(item.getId(), time);
        }
    }

    private void setupDeliveryTimeFilter() {
        cmbDeliveryTime.getItems().clear();
        cmbDeliveryTime.getItems().addAll(
                "All",
                "≤ 10 min",
                "≤ 15 min",
                "≤ 30 min",
                "≤ 45 min",
                "≤ 60 min",
                "> 60 min"
        );
        cmbDeliveryTime.setValue("All");
    }

}