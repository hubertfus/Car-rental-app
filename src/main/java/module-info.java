module com.example.carrentingapp {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.carrentalapp;
    opens com.example.carrentalapp to javafx.fxml;
}