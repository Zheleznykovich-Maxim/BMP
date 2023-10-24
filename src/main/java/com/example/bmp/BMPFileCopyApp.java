package com.example.bmp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BMPFileCopyApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Копирование BMP в буфер обмена");
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("java-icon.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(BMPFileCopyApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}