package com.example.bmp;


import com.example.bmp.Controlles.PopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Popup {

    public static void showPopup(String message, boolean isSuccess) {

        try {
            Stage successStage = new Stage();


            FXMLLoader fxmlLoader = new FXMLLoader(Popup.class.getResource("Popup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

//            successStage.initStyle(StageStyle.UTILITY); // Настройте стиль окна по вашим предпочтениям
//            successStage.initModality(Modality.APPLICATION_MODAL);

            // Получение контроллера из FXML и передача сообщения
            PopupController controller = fxmlLoader.getController();
            controller.setMessage(message);
            controller.setPopup(isSuccess);
            if (isSuccess) {
                successStage.getIcons().add(new Image(Popup.class.getResourceAsStream("imgs/green.png")));
            }
            else {
                successStage.getIcons().add(new Image(Popup.class.getResourceAsStream("imgs/error.png")));
            }

            // Отображение окна
            successStage.setScene(scene);
            successStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
