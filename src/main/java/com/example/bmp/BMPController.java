package com.example.bmp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

import java.net.MalformedURLException;

public class BMPController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void selectAndCopyBMPFile() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP Files", "*.bmp"));
        fileChooser.setTitle("Выберите файл BMP");

        java.io.File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null && selectedFile.isFile() && selectedFile.getName().toLowerCase().endsWith(".bmp")) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if (image.isError()) {
                    welcomeText.setText("Ошибка1");
                } else {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(image);
                    clipboard.setContent(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            welcomeText.setText("Ошибка2");
        }
    }
}