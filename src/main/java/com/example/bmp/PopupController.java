package com.example.bmp; // Замените на ваш пакет

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PopupController {
    @FXML
    public Button okButton;
    @FXML
    public TextFlow popupFlow;
    @FXML
    public Text popupText;


    public void setMessage(String message) {
        popupText.setText(message);
    }
    public void okButtonClick(ActionEvent event) {
        // Закрыть окно
        okButton.getScene().getWindow().hide();
    }
    public void setPopup(boolean isSuccess) {
        if (isSuccess) {
            popupFlow.getStyleClass().add("alert-success");
            okButton.getStyleClass().add("btn-success");
        }
        else {
            popupFlow.getStyleClass().add("alert-danger");
            okButton.getStyleClass().add("btn-danger");
        }
    }
}
