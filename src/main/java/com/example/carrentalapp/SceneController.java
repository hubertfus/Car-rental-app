/**
 * SceneController manages switching scenes and loading FXML elements dynamically
 * into a content HBox within the main scene.
 */
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

    /**
     * Sets the main scene and initializes the content box where FXML elements will be loaded.
     *
     * @param scene The main scene of the application.
     */
    public static void setScene(Scene scene) {
        SceneController.scene = scene;
        contentBox = (HBox) scene.getRoot().lookup("#content");
    }

    /**
     * Initializes a new FXML element and adds it to the content box.
     *
     * @param name          The name to associate with this element.
     * @param fxmlFileName  The file name of the FXML layout file.
     * @throws IOException  If there is an error loading the FXML file.
     */
    public static void initialize(String name, String fxmlFileName) throws IOException {
        Parent element = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlFileName)));
        contentBox.getChildren().add(element);
        screenMap.put(name, element);
    }

    /**
     * Adds a new FXML element to the screen map without adding it to the content box immediately.
     *
     * @param name          The name to associate with this element.
     * @param fxmlFileName  The file name of the FXML layout file.
     * @throws IOException  If there is an error loading the FXML file.
     */
    public static void addElement(String name, String fxmlFileName) throws IOException {
        Parent element = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource(fxmlFileName)));
        screenMap.put(name, element);
    }

    /**
     * Retrieves a previously loaded FXML element by name.
     *
     * @param name  The name associated with the desired FXML element.
     * @return      The FXML element corresponding to the provided name.
     * @throws SceneNotFoundException If the specified name is not found in the screen map.
     */
    public static Parent getElement(String name) {
        if (!screenMap.containsKey(name)) {
            throw new SceneNotFoundException(name);
        }
        return screenMap.get(name);
    }

    /**
     * Changes the current element displayed in the content box to the specified element.
     *
     * @param name  The name of the element to display.
     */
    public static void changeElement(String name) {
        contentBox.getChildren().setAll(getElement(name));
    }
}
