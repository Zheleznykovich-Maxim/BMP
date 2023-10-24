package com.example.bmp;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SuccessPopup {

    public void showSuccessPopup(String message) {

        try {
            Stage successStage = new Stage();

            successStage.getIcons().add(new Image(getClass().getResourceAsStream("green.png")));

            FXMLLoader fxmlLoader = new FXMLLoader(SuccessPopup.class.getResource("SuccessPopup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

//            successStage.initStyle(StageStyle.UTILITY); // Настройте стиль окна по вашим предпочтениям
//            successStage.initModality(Modality.APPLICATION_MODAL);

            // Получение контроллера из FXML и передача сообщения
            SuccessPopupController controller = fxmlLoader.getController();
            controller.setMessage(message);

            // Отображение окна
            successStage.setScene(scene);
            successStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
