/**
 * Custom exception thrown when a scene with a specified name is not found in SceneController.
 */
package com.example.carrentalapp;

public class SceneNotFoundException extends RuntimeException {
    /**
     * Constructs a SceneNotFoundException with the specified scene name.
     *
     * @param sceneName The name of the scene that was not found.
     */
    public SceneNotFoundException(String sceneName) {
        super("Scene '" + sceneName + "' not found");
    }
}