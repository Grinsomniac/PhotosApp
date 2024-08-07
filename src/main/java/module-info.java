module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.photos to javafx.fxml;
    exports com.photos;
    exports com.photos.controllers;
    opens com.photos.controllers to javafx.fxml;
}