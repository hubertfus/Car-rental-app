package com.example.carrentalapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The main application class responsible for initializing the JavaFX application.
 */
public class MainApplication extends Application {

    /**
     * Starts the JavaFX application, loading the main view and setting up scenes and styles.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Load CSS stylesheet
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("main-view.css")).toExternalForm());

        // Set the scene for SceneController to manage
        SceneController.setScene(scene);

        // Initialize SceneController with predefined scenes
        SceneController.initialize("rentedCarsScene", "rented-cars.fxml");
        SceneController.addElement("availableCarsScene", "available-cars.fxml");
        SceneController.addElement("clientsScene", "clients.fxml");

        // Configure the primary stage
        stage.setTitle("Car Renting App");
        stage.setScene(scene);
        stage.minHeightProperty().setValue(480);
        stage.minWidthProperty().setValue(640);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }
}