package com.rag.chatbotui.utils;

import com.rag.chatbotui.scenes.ChatScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;

    // Set the primary stage
    public static void setStage(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        }
    }

    // Switch to a scene
    // No change needed here, it already works for any Scene type
    public static void switchScene(Scene scene) {
        if (scene != null && primaryStage != null) {
            primaryStage.setScene(scene);
        } else {
            System.err.println("Scene or primaryStage is null.");
        }
    }


    // Optionally, you could add a method to get the current scene
    public static Scene getCurrentScene() {
        if (primaryStage != null) {
            return primaryStage.getScene();
        }
        return null;
    }
}
