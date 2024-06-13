package com.example.carrentalapp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NavigationController {

    @FXML
    private HBox navigationBar;

    @FXML
    public void initialize() {
        for (Node node : navigationBar.getChildren()) {
            if (node instanceof Label navItem) {
                navItem.setOnMouseClicked(e -> handleNavigation(navItem.getUserData().toString()));
            }
        }
    }

    private void handleNavigation(String sceneName) {
        SceneController.changeElement(sceneName);
    }
}
