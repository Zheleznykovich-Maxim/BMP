package com.example.bmp.Controlles;

import com.example.bmp.ClipboardHistoory;
import com.example.bmp.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import static com.example.bmp.Filter.*;
import static com.example.bmp.Validation.isValidNumber;
import static com.example.bmp.Validation.showAlert;

public class BMPController {

    @FXML
    public Button copyButton;
    @FXML
    public RadioButton bmpButton, pngButton, jpgButton;
    public TextField imgHeight, imgWidth;
    public Button previewButton;
    public CheckBox bwButton, sepiaButton, contrastButton, brightnessButton, distortionButton;
    public CheckBox blurButton;
    public RadioButton lightBlurButton, middleBlurButton, strongBlurButton;
    public Label inputContrast;
    public Label inputBrightness;
    public ComboBox distortionBox;
    public Slider sliderContrast;
    public Slider sliderBrightness;
    public ListView ListViewClipboard;
    public Button clearAllButton;
    private java.io.File selectedFile;
    private boolean isBW, isSepia, isContrast, isBrightness, isDistortion, isBlur;
    private int blurValue;
    private int chosenDistortion;
    private Image selectedImage;
    @FXML
    private Label fileName;

    public static ObservableList<String> clipboardItems = FXCollections.observableArrayList();
     // Для отслеживания уникальных элементов

    public void initialize() {
        distortionBox.getItems().addAll(1, 2, 3, 4, 5);
        inputContrast.textProperty().bind(sliderContrast.valueProperty().asString("%.0f"));
        inputBrightness.textProperty().bind(sliderBrightness.valueProperty().asString("%.0f"));
        // Обработка изменений в буфере обмена
        ListViewClipboard.setItems(clipboardItems);

        ListViewClipboard.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
                }
            }
        });
        ClipboardHistoory.start();
    }
    @FXML
    private void clearAllClipboard(){
        ListViewClipboard.getItems().clear();
    }
    @FXML
    private void deleteSelected() {
        final int selectedId = ListViewClipboard.getSelectionModel().getSelectedIndex();
        if (selectedId != -1) {
            ListViewClipboard.getItems().remove(selectedId);
        }
    }

    @FXML
    protected void selectFile() throws MalformedURLException {
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
                fileName.setText("");
            } else {
                selectedImage = image;
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
            showAlert("Ошибка", "Введите целое число в диапазоне [1, 1000]");
        } else {
            // Если новое значение допустимо, обновляем предыдущее значение
            if (selectedFile != null) {
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    if (image.isError()) {
                        Popup.showPopup("Ошибка в копировании изображения в буфер обмена.",false);
                    } else {
                        Image baseImage = selectedImage;
                        ApplyImageChanges();
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(selectedImage);
                        clipboard.setContent(content);
                        selectedImage = baseImage;
                        //Вызвать всплывающее окно успеха
                        ClipboardHistoory.addToClipboard(selectedFile.getName());
                        Popup.showPopup("Файл успешно скопирован в буфер обмена.", true);
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
            isBW = false;
        }

    }
    @FXML
    private void filterSepia() {
        if (sepiaButton.isSelected()) {
            isSepia = true;
        }
        else {
            isSepia = false;
        }
    }
    @FXML
    private void unlockBlur() {
        if (blurButton.isSelected()) {
            lightBlurButton.setDisable(false);
            middleBlurButton.setDisable(false);
            strongBlurButton.setDisable(false);

        }
        else {
            isBlur = false;
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
//            if (!isValidNumber(inputContrast.getText(), 1.0, 3.0)) {
//                showAlert("Ошибка в значении контрастности", "Введите вещественное число в диапазоне [1.0, 3.0]");
//                contrastButton.setSelected(false);
//            }
//            else {
                isContrast = true;
//                inputContrast.setDisable(true);
//            }

        }
        else {
//            inputContrast.setDisable(false);
            isContrast = false;
        }

    }
    @FXML
    private void filterBrightness() {
        if (brightnessButton.isSelected()) {
//            isContrast = selectedImage;
//            if (!isValidNumber(inputBrightness.getText(), -3.0, 3.0)) {
//                showAlert("Ошибка в значении яркостити", "Введите вещественное число в диапазоне [-3.0, 3.0]");
//                brightnessButton.setSelected(false);
//            }
//            else {
                isBrightness = true;
//                inputBrightness.setDisable(true);
//            }
        }
        else {
//            inputBrightness.setDisable(false);
            isBrightness = false;

        }
    }
    @FXML
    private void filterDistortion() {
        if (distortionButton.isSelected()) {
            isDistortion = true;
        }
        else {
//            selectedImage = isBW;
            isDistortion = false;
        }
    }
    @FXML
    private void chooseDistortion() {
        chosenDistortion = Integer.parseInt(distortionBox.getSelectionModel().getSelectedItem().toString());
    }
    @FXML
    private void showPreviewImage() {
        if (!isValidNumber(imgHeight.getText(), 1, 1000)
                || !isValidNumber(imgWidth.getText(), 1, 1000)) {
            showAlert("Ошибка в размерах изображения", "Введите целое число в диапазоне [1, 1000]");
        }
        else {
            Image baseImage = selectedImage;
            ApplyImageChanges();
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
    private void ApplyImageChanges() {
        resizeImage(selectedImage);
        if (isBW) {
            selectedImage = BlackAndWhiteFilter(selectedImage);
        }
        if (isSepia) {
            selectedImage = SepiaFilter(selectedImage);
        }
        if (isContrast) {
            selectedImage = ContrastFilter(selectedImage, inputContrast);
        }
        if (isBrightness) {
            selectedImage = BrightnessFilter(selectedImage, inputBrightness);
        }
        if (isDistortion) {
            selectedImage = DistortionFilter(selectedImage, chosenDistortion);
        }
        if (isBlur) {
            selectedImage = BlurFilter(selectedImage, blurValue);
        }
    }
}