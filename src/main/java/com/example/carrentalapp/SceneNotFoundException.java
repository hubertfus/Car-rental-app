package com.example.carrentalapp;

public class SceneNotFoundException extends RuntimeException {
    public SceneNotFoundException(String sceneName) {
        super("Scene '" + sceneName + "' not found");
    }
}
