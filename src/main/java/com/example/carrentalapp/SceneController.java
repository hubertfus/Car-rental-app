package com.example.carrentalapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SceneController {
    private static HashMap<String, Parent> screenMap = new HashMap<>();
    private static Scene scene;
    private static HBox contentBox;

    public static void setScene(Scene scene) {
        SceneController.scene = scene;
        contentBox = (HBox) scene.getRoot().lookup("#content");
    }

    public static void initialize(String name, String fxmlFileName) throws IOException {
        Parent element = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlFileName)));
        contentBox.getChildren().add(element);
        screenMap.put(name, element);
    }

    public static void addElement(String name, String fxmlFileName) throws IOException {
        Parent element = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlFileName)));
        screenMap.put(name, element);
    }

    public static Parent getElement(String name) {
        if (!screenMap.containsKey(name)) {
            throw new SceneNotFoundException(name);
        }
        return screenMap.get(name);
    }

    public static void changeElement(String name) {
        contentBox.getChildren().setAll(getElement(name));
    }
}
