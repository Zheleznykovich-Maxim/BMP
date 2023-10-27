package com.example.bmp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

import java.net.MalformedURLException;

public class BMPController {

    @FXML
    public Button copyButton;
    @FXML
    public RadioButton bmpButton, pngButton, jpgButton;
    @FXML
    public Label formatLabel;
    public TextField imgHeight;
    public TextField imgWidth;
    private java.io.File selectedFile;

    @FXML
    private Label fileName;

    @FXML
    protected void selectAndCopyBMPFile() throws MalformedURLException {
        fileName.setText("");
        copyButton.setDisable(true);
        imgHeight.setDisable(true);
        imgWidth.setDisable(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP Files", "*.bmp"));
        fileChooser.setTitle("Выберите файл BMP");

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null && selectedFile.isFile() && selectedFile.getName().toLowerCase().endsWith(".bmp")) {

            fileName.setText(selectedFile.getName());

            Image image = new Image(selectedFile.toURI().toString());
            if (image.isError()) {
                Popup.showPopup("Ошибка в файле изображения.", false);
            } else {
                copyButton.setDisable(false);
                imgHeight.setDisable(false);
                imgHeight.setText(String.valueOf((int)image.getHeight()));
                imgWidth.setDisable(false);
                imgWidth.setText(String.valueOf((int)image.getWidth()));
            }
        } else {
            Popup.showPopup("Ошибка: не выбран файл!", false);
        }
    }
    @FXML
    protected void onCopyClick() {
        if (!isValidNumber(imgHeight.getText()) || !isValidNumber(imgWidth.getText())) {
            // Если новое значение недопустимо, восстанавливаем предыдущее значение
//            imgHeight.setText(previousValue);
            imgHeight.getStyleClass().add("");
            showAlert("Ошибка", "Введите целое число в диапазоне [1, 1000]");
        } else {
            // Если новое значение допустимо, обновляем предыдущее значение
            if (selectedFile != null) {
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    if (image.isError()) {
                        Popup.showPopup("Ошибка в копировании изображения в буфер обмена.",false);
                    } else {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();

                        content.putImage(resizeImage(image));
                        clipboard.setContent(content);

                        //Вызвать всплывающее окно успеха
                        Popup.showPopup("Файл успешно скопирован в буфер обмена.", true);
                        //                    fileName.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Popup.showPopup("Ошибка: не выбран файл!", false);
            }
        }


    }
    @FXML
    public void getFormat() {

        if (bmpButton.isSelected()){
            formatLabel.setText(bmpButton.getText());
        }
        else if (pngButton.isSelected()) {
            formatLabel.setText(pngButton.getText());
        }
        else if (jpgButton.isSelected()) {
            formatLabel.setText(jpgButton.getText());
            
        }
    }

    public WritableImage resizeImage(Image image) {
        String heightText = imgHeight.getText(); // Получите текст из TextField
        String widthText = imgWidth.getText();

        int height = Integer.parseInt(imgHeight.getText()); // Попытайтесь преобразовать текст в int
        int width = Integer.parseInt(imgWidth.getText());
        // Ваш код для работы с intValue

        WritableImage resizedImage = new WritableImage(width, height);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//            imageView.setPreserveRatio(true);
        imageView.snapshot(null, resizedImage);
        return resizedImage;

    }
    private boolean isValidNumber(String text) {
        try {
            int value = Integer.parseInt(text);
            return value >= 1 && value <= 1000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}