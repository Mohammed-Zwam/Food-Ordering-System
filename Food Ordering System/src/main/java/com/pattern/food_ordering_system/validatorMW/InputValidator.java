package com.pattern.food_ordering_system.validatorMW;

import com.pattern.food_ordering_system.utils.InputParser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;

public class InputValidator {
    public static void isEmptyOrNull(TextInputControl input) {
        if (InputParser.toString(input).isEmpty() || input == null) throw new RuntimeException("please enter your " + input.getPromptText());
    }

    public static void isWeekPassword(TextInputControl input) {
        if (InputParser.toString(input).length() < 8) throw new RuntimeException("Password must be at least 8 characters long.");
    }

    public static void isPasswordMatching(TextInputControl password, TextInputControl confirmPassword) {
        if (!InputParser.toString(password).equals(InputParser.toString(confirmPassword))) throw new RuntimeException("Passwords do not match.");
    }
    public static void isPhoneNumber(TextInputControl input) {
        if (!InputParser.toString(input).matches("\\d{11}")) throw new RuntimeException("Please enter a valid phone number.");
    }

    public static void isSelected(ToggleGroup group) {
        if (group.getSelectedToggle() == null) {
            throw new RuntimeException("Please select an option");
        }
    }

    public static void isValidPrice(double price) {
        if (price <= 0) {
            throw new RuntimeException("Please enter a valid price");
        }
    }

    public static void isSelected(ComboBox<String> comboBox) {
        if (comboBox.getValue() == null) {
            throw new RuntimeException("Please select an option");
        }
    }

}
