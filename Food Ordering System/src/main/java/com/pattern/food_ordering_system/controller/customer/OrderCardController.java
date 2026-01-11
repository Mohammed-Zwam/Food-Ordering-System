package com.pattern.food_ordering_system.controller.customer;

import com.pattern.food_ordering_system.entity.OrderItem;
import com.pattern.food_ordering_system.model.customer.CustomerOrder;
import com.pattern.food_ordering_system.model.status.OrderStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import com.pattern.food_ordering_system.entity.Review;
import com.pattern.food_ordering_system.repository.ReviewRepo;
import com.pattern.food_ordering_system.model.user.UserFactory;
import org.controlsfx.control.Rating;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderCardController {
    @FXML
    private Label lblDate;
    @FXML
    private Label lblRestaurantName;
    @FXML
    private Label lblItemsCount;
    @FXML
    private Label lblTotal;
    @FXML
    private ImageView imgRestaurant;

    @FXML
    private VBox ORDER_PLACED, CONFIRMED, BEING_PREPARED, OUT_FOR_DELIVERY, DELIVERED;

    @FXML
    private Button rateBtn;

    private CustomerOrder order;

    public void setOrderData(CustomerOrder order) {
        this.order = order;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd • hh:mm a", Locale.ENGLISH);
        lblDate.setText(order.getOrderTime().format(formatter));
        lblTotal.setText(String.format(Locale.US, "EGP %.2f", order.getTotalPriceWithFee()));
        int count = order.getItems() != null ? order.getItems().size() : 0;
        lblItemsCount.setText(count + " items");
        lblRestaurantName.setText(order.getRestaurantName());
        rateBtn.setVisible(order.getStatus().equals(OrderStatus.DELIVERED));

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            if (!(order.getRestaurantLogo() == null || order.getRestaurantLogo().equalsIgnoreCase("default"))) {
                Image image = new Image(Paths.get(System.getProperty("user.dir") + order.getRestaurantLogo()).toUri().toString());
                imgRestaurant.setImage(image);
            }

            setupItemsTooltip(order.getItems());
            setActiveStatues();
        }

        if (order.getReview() != null) {
            rateBtn.setText("Read your review");
        }
    }


    private void setupItemsTooltip(List<OrderItem> items) {
        Tooltip tooltip = new Tooltip();
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(200));

        tooltip.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
        VBox content = createTooltipContent(items);
        tooltip.setGraphic(content);
        lblItemsCount.setTooltip(tooltip);
        lblItemsCount.setStyle("-fx-cursor: hand; -fx-underline: true; -fx-text-fill: #1E88E5;");
    }

    private VBox createTooltipContent(List<OrderItem> items) {
        VBox container = new VBox(8);
        container.setMaxHeight(200);

        container.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 2); " +
                "-fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-height: 100px");

        Label header = new Label("Order Details");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #1E88E5;");
        container.getChildren().add(header);

        for (OrderItem item : items) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            Label nameQty = new Label(item.getQuantity() + "x " + item.getFoodItemName());
            nameQty.setStyle("-fx-font-size: 11; -fx-text-fill: #333;");
            nameQty.setPrefWidth(120);
            nameQty.setWrapText(true);

            double totalItemPrice = item.getQuantity() * item.getPrice();
            Label price = new Label(String.format("%.0f EGP", totalItemPrice));
            price.setStyle("-fx-font-weight: bold; -fx-font-size: 11; -fx-text-fill: #4CAF50;");

            row.getChildren().addAll(nameQty, price);
            container.getChildren().add(row);
        }

        return container;
    }

    private void setActiveStatues() {
        VBox[] stages = {ORDER_PLACED, CONFIRMED, BEING_PREPARED, OUT_FOR_DELIVERY, DELIVERED};
        int idx = order.getStatus().ordinal();
        if (idx >= 3) idx--;
        for (int i = 0; i <= idx; i++) {
            stages[i].setOpacity(1);
        }
        for (int i = idx + 1; i < 5; i++) {
            stages[i].setOpacity(0.1);
        }
    }


    @FXML
    private void onRate() {
        Review existingReview = order.getReview();

        if (existingReview != null) {
            showReadOnlyReview(existingReview);
        } else {
            showRatingDialog();
        }
    }

    private void showRatingDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("Rate Your Meal");
        dialog.setHeaderText("How was your order from " + order.getRestaurantName() + "?");

        ButtonType submitBtn = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitBtn, ButtonType.CANCEL);

        VBox content = new VBox(10);

        Rating ratingControl = new Rating();
        ratingControl.setMax(5);
        ratingControl.setRating(5);

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Write your comment here...");
        commentArea.setWrapText(true);
        commentArea.setPrefRowCount(3);

        content.getChildren().addAll(new Label("Rating:"), ratingControl, new Label("Comment:"), commentArea);
        dialog.getDialogPane().setContent(content);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == submitBtn) {
            double ratingValue = ratingControl.getRating();
            String commentText = commentArea.getText();
            long customerId = UserFactory.getUser().getId();

            Review review = new Review(ratingValue, commentText);
            boolean success = ReviewRepo.addReview(
                    order.getOrderId(),
                    customerId,
                    order.getRestaurantId(),
                    review
            );
            order.setReview(review);
            setOrderData(this.order);


            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Thank you for your feedback!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not save review.");
            }
        }
    }

    private void showReadOnlyReview(Review review) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Your Review");
        dialog.setHeaderText("You have already rated this order.");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(10);
        Rating ratingControl = new Rating();
        ratingControl.setMax(5);
        ratingControl.setRating(review.getRating());
        ratingControl.setMouseTransparent(true);
        ratingControl.setFocusTraversable(false);

        TextArea commentArea = new TextArea();
        commentArea.setText(review.getComment());
        commentArea.setEditable(false);
        commentArea.setWrapText(true);

        content.getChildren().addAll(new Label("Your Rating:"), ratingControl, new Label("Your Comment:"), commentArea);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}