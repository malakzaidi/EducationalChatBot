package com.rag.chatbotui;
import com.rag.chatbotui.ChatbotUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginScreen extends VBox {

    private ChatbotUI application;

    public LoginScreen(ChatbotUI application) {
        this.application = application;

        setAlignment(Pos.CENTER);
        setSpacing(10);
        setPadding(new Insets(20));
        getStyleClass().add("login-screen");

        Label titleLabel = new Label("Chatbot");
        titleLabel.getStyleClass().add("title-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("text-field");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");
        loginButton.setOnAction(e -> application.showChatScreen());

        Button signupButton = new Button("Sign Up");
        signupButton.getStyleClass().add("button");
        signupButton.setOnAction(e -> showSignupForm());

        getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, signupButton);
    }

    private void showSignupForm() {
        // Implement signup functionality here
        System.out.println("Show signup form");
    }
}

