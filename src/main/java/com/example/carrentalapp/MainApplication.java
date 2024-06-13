package com.example.carrentalapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SceneNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("main-view.css")).toExternalForm());
        SceneController.setScene(scene);

        SceneController.initialize("rentedCarsScene", "rented-cars.fxml");
        SceneController.addElement("availableCarsScene", "available-cars.fxml");
        SceneController.addElement("clientsScene", "clients.fxml");


        stage.setTitle("Car Renting App");
        stage.setScene(scene);
        stage.minHeightProperty().setValue(480);
        stage.minWidthProperty().setValue(640);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
