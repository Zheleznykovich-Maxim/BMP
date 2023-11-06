module BMP {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.bmp to javafx.fxml;
    exports com.example.bmp;
    exports com.example.bmp.Controlles;
    opens com.example.bmp.Controlles to javafx.fxml;
}