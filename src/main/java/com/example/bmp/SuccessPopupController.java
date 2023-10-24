package com.example.bmp; // Замените на ваш пакет

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SuccessPopupController {
    @FXML
    public Text successMessageText;
    public Button okButton;


    public void setMessage(String message) {
        successMessageText.setText(message);
    }
    public void okButtonClick(ActionEvent event) {
        // Закрыть окно
        okButton.getScene().getWindow().hide();
    }
}
