package com.example.bmp;

import javafx.application.Platform;
import static com.example.bmp.Controlles.BMPController.clipboardItems;

public class ClipboardHistoory {
    private static String lastItem = "";
    public static void start() {
        Thread clipboardMonitor = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    Object clipboardContent = getClipboardContent();
                    if (clipboardContent != null && !lastItem.equals(clipboardContent.toString())) {
                        addToClipboard(clipboardContent.toString());
                        lastItem = clipboardContent.toString();
                    }
                });

                try {
                    Thread.sleep(1000); // Опрашивать буфер обмена каждую секунду
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clipboardMonitor.setDaemon(true);
        clipboardMonitor.start();
    }
    private static Object getClipboardContent() {
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        if (clipboard.hasFiles()) {
            return clipboard.getFiles().get(0);
        } else if (clipboard.hasString()) {

            return clipboard.getString();
        }
        return null;
    }


    public static void addToClipboard(String text) {
        // Используем уникальный идентификатор для отслеживания добавленных элементов
        clipboardItems.add(text);
    }
}
