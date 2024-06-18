package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Controller class for handling navigation within the application.
 */
public class NavigationController {

    @FXML
    private HBox navigationBar;

    /**
     * Initializes the navigation bar by setting click handlers for each navigation item (Label).
     * Each item's click will trigger a scene change based on its associated scene name.
     */
    @FXML
    public void initialize() {
        for (Node node : navigationBar.getChildren()) {
            if (node instanceof Label navItem) {
                navItem.setOnMouseClicked(e -> handleNavigation(navItem.getUserData().toString()));
            }
        }
    }

    /**
     * Handles the navigation action when a navigation item is clicked.
     *
     * @param sceneName The name of the scene to navigate to.
     */
    private void handleNavigation(String sceneName) {
        SceneController.changeElement(sceneName);
    }
}
