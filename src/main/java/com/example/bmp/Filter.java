package com.example.bmp;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import java.awt.*;

public class Filter {
    public static WritableImage BlackAndWhiteFilter(Image selectedImage) {
        ImageView imageView = new ImageView(selectedImage);
        WritableImage bwImage = new WritableImage(
                (int)selectedImage.getWidth(),
                (int)selectedImage.getHeight());
        // Применение эффекта ColorAdjust для получения чёрно-белого изображения
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0); // Установите насыщенность в -1, чтобы получить чёрно-белое изображение
        imageView.setEffect(colorAdjust);
        imageView.snapshot(null, bwImage);
        return bwImage;
    }
    public static WritableImage ContrastFilter(Image selectedImage, TextField inputContrast) {
        Image testImage = selectedImage;
//        selectedImage = contrastBaseImage;
        double contrastFactor = Double.parseDouble(inputContrast.getText());
        int width = (int) selectedImage.getWidth();
        int height = (int) selectedImage.getHeight();
        WritableImage contrastedImage = new WritableImage(width, height);
        PixelReader pixelReader = testImage.getPixelReader();
        PixelWriter pixelWriter = contrastedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double red = (color.getRed() - 0.5) * contrastFactor + 0.5;
                double green = (color.getGreen() - 0.5) * contrastFactor + 0.5;
                double blue = (color.getBlue() - 0.5) * contrastFactor + 0.5;

                red = Math.min(1.0, Math.max(0.0, red));
                green = Math.min(1.0, Math.max(0.0, green));
                blue = Math.min(1.0, Math.max(0.0, blue));

                Color newColor = Color.color(red, green, blue, color.getOpacity());
                pixelWriter.setColor(x, y, newColor);
            }
        }

        return contrastedImage;
    }
    public static Image BrightnessFilter(Image originalImage, TextField inputBrightness) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();
        double brightnessFactor = Double.parseDouble(inputBrightness.getText());
        WritableImage brightenedImage = new WritableImage(width, height);
        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = brightenedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double red = color.getRed() + brightnessFactor / 3;
                double green = color.getGreen() + brightnessFactor / 3;
                double blue = color.getBlue() + brightnessFactor / 3;

                // Ограничьте значения каналов до диапазона [0, 1]
                red = Math.min(1.0, Math.max(0.0, red));
                green = Math.min(1.0, Math.max(0.0, green));
                blue = Math.min(1.0, Math.max(0.0, blue));

                Color newColor = Color.color(red, green, blue, color.getOpacity());
                pixelWriter.setColor(x, y, newColor);
            }
        }

        return brightenedImage;
    }
    public static WritableImage BlurFilter(Image selectedImage, int blurValue) {

        ImageView imageView = new ImageView(selectedImage);
        WritableImage bluredImage = new WritableImage(
                (int)selectedImage.getWidth() - 20,
                (int)selectedImage.getHeight() - 20);
        // Применение эффекта ColorAdjust для получения размытого изображения
        GaussianBlur mediumBlur = new GaussianBlur();
        mediumBlur.setRadius(blurValue);
//        imageView.setPreserveRatio(true);
        imageView.setEffect(mediumBlur);
        // Определяем область, которую нужно захватить (обрезать)
        SnapshotParameters params = new SnapshotParameters();

        // Определяем область, которую нужно захватить (обрезать)
        int x = 10; // Левая граница
        int y = 10; // Верхняя граница
        int width = (int) (imageView.getBoundsInLocal().getWidth()) - x;
        int height = (int) (imageView.getBoundsInLocal().getHeight()) - y;
        params.setViewport(new javafx.geometry.Rectangle2D(x, y, width, height));

        imageView.snapshot(params, bluredImage);

        return bluredImage;
    }

}
