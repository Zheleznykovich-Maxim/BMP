package com.example.bmp;

import javafx.scene.control.Alert;

public class Validation {
    public static boolean isValidNumber(String text, int min, int max) {
        try {
            int value = Integer.parseInt(text);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidNumber(String text, double min, double max) {
        try {
            double value = Double.parseDouble(text);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
