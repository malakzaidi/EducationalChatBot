package com.rag.chatbotui;

import javafx.application.Application;
import com.rag.chatbotui.scenes.*;
import javafx.application.Platform;
import javafx.stage.Stage;


public class JavaFxApplication extends Application {
    private Stage primaryStage;
    private LoginScene loginScene;
    private RegisterScene registerScene;
    private ChatScene chatScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize scenes
        loginScene = new LoginScene();
        registerScene = new RegisterScene();
        chatScene = new ChatScene();

        // Set up navigation handlers
        setupNavigationHandlers();

        // Set initial scene to login
        primaryStage.setTitle("ChatBot Application");
        primaryStage.setScene(loginScene.getScene());
        primaryStage.show();

        // Simulate auto-login after 2 seconds
        Platform.runLater(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showChat();
            addExampleMessages();
        });
    }

    private void setupNavigationHandlers() {
        loginScene.getRegisterLink().setOnAction(e -> showRegister());
        registerScene.getLoginLink().setOnAction(e -> showLogin());
        loginScene.getLoginButton().setOnAction(e -> showChat());
        registerScene.getRegisterButton().setOnAction(e -> showLogin());
    }

    private void showLogin() {
        primaryStage.setScene(loginScene.getScene());
    }

    private void showRegister() {
        primaryStage.setScene(registerScene.getScene());
    }

    private void showChat() {
        primaryStage.setScene(chatScene.getScene());
    }

    private void addExampleMessages() {
        chatScene.addMessage("Hello! How can I assist you today?", false);
        chatScene.addMessage("Hi! I have a question about my account.", true);
        chatScene.addMessage("Of course! What would you like to know about your account?", false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

