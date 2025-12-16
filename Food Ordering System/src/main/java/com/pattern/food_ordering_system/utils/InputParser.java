package com.pattern.food_ordering_system.utils;

import javafx.scene.control.TextInputControl;

public class InputParser {
        public static int toInt(TextInputControl input) {
            try {
                return Integer.parseInt(toString(input));
            } catch (NumberFormatException e) {
                throw new RuntimeException(input.getPromptText() + " must be a valid integer", e);
            }
        }

        public static double toDouble(TextInputControl input) {
            String fieldName = input.getPromptText() != null ? input.getPromptText() : "Input Field";
            try {
                return Double.parseDouble(toString(input));
            } catch (NumberFormatException e) {
                throw new RuntimeException(input.getPromptText() + " must be a valid integer", e);
            }
        }

        public static String toString(TextInputControl input) {
            return input.getText().trim();
        }
}
