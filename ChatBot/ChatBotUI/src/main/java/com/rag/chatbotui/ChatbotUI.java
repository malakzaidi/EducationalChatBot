package com.rag.chatbotui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class ChatbotUI extends Application {

    private StackPane root;
    private LoginScreen loginScreen;
    private ChatScreen chatScreen;

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        loginScreen = new LoginScreen(this);
        chatScreen = new ChatScreen(this);

        root.getChildren().add(loginScreen);

        Scene scene = new Scene(root, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Chatbot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showChatScreen() {
        root.getChildren().clear();
        root.getChildren().add(chatScreen);
    }

    public void showLoginScreen() {
        root.getChildren().clear();
        root.getChildren().add(loginScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


