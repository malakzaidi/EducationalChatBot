package com.rag.chatbotui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Component;

@Component
public class MyJavaFxController {

    public Scene createScene() {
        // Example simple scene
        StackPane root = new StackPane(new Label("Hello, Spring Boot with JavaFX!"));
        return new Scene(root, 800, 600);
    }
}
