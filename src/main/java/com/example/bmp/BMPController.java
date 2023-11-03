package com.example.bmp;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.MalformedURLException;

import static com.example.bmp.Filter.*;
import static com.example.bmp.Validation.isValidNumber;
import static com.example.bmp.Validation.showAlert;

public class BMPController {

    @FXML
    public Button copyButton;
    @FXML
    public RadioButton bmpButton, pngButton, jpgButton;
    public TextField imgHeight;
    public TextField imgWidth;

    @FXML
    public Button previewButton;
    public CheckBox bwButton;
    public RadioButton lightBlurButton;
    public RadioButton middleBlurButton;
    public RadioButton strongBlurButton;
    public CheckBox blurButton;
    public TextField inputContrast;
    public CheckBox contrastButton;
    public TextField inputBrightness;
    public CheckBox brightnessButton;
    private java.io.File selectedFile;
    private boolean isBW;
    private boolean isBlur;
    private int blurValue;
    private int contrastValue;
    private boolean isContrast;
    private boolean isBrightness;
    private Image selectedImage;
    @FXML
    private Label fileName;

    @FXML
    protected void selectAndCopyBMPFile() throws MalformedURLException {
        fileName.setText("");
        copyButton.setDisable(true);
        imgHeight.setDisable(true);
        imgWidth.setDisable(true);
        FileChooser fileChooser = new FileChooser();
        String[] format = getFormat();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(format[0], format[1]));
        fileChooser.setTitle("Выберите файл BMP");

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null && selectedFile.isFile() && selectedFile.getName().toLowerCase().endsWith(format[1].substring(1))) {

            fileName.setText(selectedFile.getName());

            Image image = new Image(selectedFile.toURI().toString());
            if (image.isError()) {
                Popup.showPopup("Ошибка в файле изображения.", false);
            } else {
                selectedImage = image;
//                isBW = image;
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
        if (!isValidNumber(imgHeight.getText(), 1, 1000)
                || !isValidNumber(imgWidth.getText(), 1, 1000)) {
            // Если новое значение недопустимо, восстанавливаем предыдущее значение
//            imgHeight.setText(previousValue);
//            imgHeight.getStyleClass().add("");
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

                        content.putImage(resizeImage(selectedImage));
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
    public String[] getFormat() {
        String[] format = new String[2];
        if (bmpButton.isSelected()){
            format[0] = "BMP Files";
            format[1] = "*.bmp";
        }
        else if (pngButton.isSelected()) {
            format[0] = "PNG Files";
            format[1] = "*.png";
        }
        else if (jpgButton.isSelected()) {
            format[0] = "JPG Files";
            format[1] = "*.jpg";
        }
        return format;
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
        selectedImage = resizedImage;
        return resizedImage;

    }
    @FXML
    private void filterBlackAndWhite() {

        if (bwButton.isSelected()) {
            isBW = true;
        }
        else {
//            selectedImage = isBW;
            isBW = false;
        }

    }
    @FXML
    private void unlockBlur() {
        if (blurButton.isSelected()) {
//            isBlur = selectedImage;

            lightBlurButton.setDisable(false);
            middleBlurButton.setDisable(false);
            strongBlurButton.setDisable(false);

        }
        else {
//            selectedImage = isBlur;
            isBlur = false;
//            if (bwButton.isSelected()) {
//                filterBlackAndWhite();
//            }
            lightBlurButton.setDisable(true);
            lightBlurButton.setSelected(false);
            middleBlurButton.setDisable(true);
            middleBlurButton.setSelected(false);
            strongBlurButton.setDisable(true);
            strongBlurButton.setSelected(false);
        }
    }
    @FXML
    private void blurImage() {
//        isBlur = selectedImage;
        isBlur = true;
        if (lightBlurButton.isSelected()){
            blurValue = 5;
        } else if (middleBlurButton.isSelected()) {
            blurValue = 10;
        } else if (strongBlurButton.isSelected()) {
            blurValue = 20;
        }
        else {
            isBlur = false;
        }
    }

    @FXML
    private void filterContrast() {
        if (contrastButton.isSelected()) {
//            isContrast = selectedImage;
            if (!isValidNumber(inputContrast.getText(), 1.0, 3.0, false)) {
                showAlert("Ошибка в значении контрастности", "Введите вещественное число в диапазоне [1.0, 3.0]");
                contrastButton.setSelected(false);
            }
            else {
                isContrast = true;
            }
        }
        else {
//            selectedImage = isContrast;
            isContrast = false;
//            if (bwButton.isSelected()) {
//                filterBlackAndWhite();
//            }
        }

    }
    @FXML
    private void filterBrightness() {
        if (brightnessButton.isSelected()) {
//            isContrast = selectedImage;
            if (!isValidNumber(inputBrightness.getText(), 1.0, 3.0, true)) {
                showAlert("Ошибка в значении яркости", "Введите вещественное число в диапазоне [-3.0, 3.0]");
                brightnessButton.setSelected(false);
            }
            else {
                isBrightness = true;
            }
        }
        else {
//            selectedImage = isContrast;
            isBrightness = false;
//            if (bwButton.isSelected()) {
//                filterBlackAndWhite();
//            }
        }
    }

    @FXML
    private void showPreviewImage() {
        if (!isValidNumber(imgHeight.getText(), 1, 1000)
                || !isValidNumber(imgWidth.getText(), 1, 1000)) {
            // Если новое значение недопустимо, восстанавливаем предыдущее значение
//            imgHeight.setText(previousValue);
//            imgHeight.getStyleClass().add("");
            showAlert("Ошибка в размерах изображения", "Введите целое число в диапазоне [1, 1000]");
        }
        else {
            Image baseImage = selectedImage;
            resizeImage(selectedImage);
            if (isBW) {
                selectedImage = BlackAndWhiteFilter(selectedImage);
            }
            if (isContrast) {
                selectedImage = ContrastFilter(selectedImage, inputContrast);
                contrastButton.setSelected(false);
                isContrast = false;
            }
            if (isBrightness) {
                selectedImage = BrightnessFilter(selectedImage, inputBrightness);
                brightnessButton.setSelected(false);
                isBrightness = false;
            }
            if (isBlur) {
                selectedImage = BlurFilter(selectedImage, blurValue);
            }
            Stage previewStage = new Stage();
            ImageView previewImageView = new ImageView(selectedImage);
            StackPane stackPane = new StackPane(previewImageView);
            Scene previewScene = new Scene(stackPane, selectedImage.getWidth(), selectedImage.getHeight());

            previewStage.setTitle("Предварительный просмотр");
            previewStage.setScene(previewScene);
            previewStage.show();
            selectedImage = baseImage;
        }

    }


}