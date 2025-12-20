module com.example.food_ordering_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires org.json;

    opens com.pattern.food_ordering_system.entity to javafx.base;
    opens com.pattern.food_ordering_system to javafx.fxml;
    exports com.pattern.food_ordering_system;
    opens com.pattern.food_ordering_system.controller.restaurant to javafx.fxml;
    opens com.pattern.food_ordering_system.controller.registration to javafx.fxml;
    opens com.pattern.food_ordering_system.controller.customer to javafx.fxml;
    opens com.pattern.food_ordering_system.controller.delivery to javafx.fxml;
}