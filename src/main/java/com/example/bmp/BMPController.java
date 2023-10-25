package com.example.bmp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import java.net.MalformedURLException;

public class BMPController {

    private java.io.File selectedFile;
    @FXML
    private Label welcomeText;

    @FXML
    private Label fileName;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void selectAndCopyBMPFile() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP Files", "*.bmp"));
        fileChooser.setTitle("Выберите файл BMP");

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null && selectedFile.isFile() && selectedFile.getName().toLowerCase().endsWith(".bmp")) {
            fileName.setText(selectedFile.getName());

        } else {
            Popup.showSuccessPopup("Ошибка: не выбран файл!", false);
        }
    }
    @FXML
    protected void onCopyClick() {
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                if (image.isError()) {
                    Popup.showSuccessPopup("Ошибка в копировании изображения в буфер обмена.",false);
                } else {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(image);
                    clipboard.setContent(content);

                    //Вызвать всплывающее окно успеха
                    Popup.showSuccessPopup("Файл успешно скопирован в буфер обмена.", true);
                    fileName.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}