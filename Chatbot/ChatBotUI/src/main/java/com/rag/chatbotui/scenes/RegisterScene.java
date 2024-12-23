package com.rag.chatbotui.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RegisterScene {
    private Scene scene;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField emailField;
    private Hyperlink loginLink;
    private Button registerButton;

    public RegisterScene() {
        // Create main container
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        // Email field
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Username field
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Confirm Password field
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(300);
        confirmPasswordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Register button
        registerButton = new Button("Register");
        registerButton.setStyle(
                "-fx-background-color: #2196f3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10 20;"
        );
        registerButton.setMaxWidth(300);

        // Login link
        loginLink = new Hyperlink("Already have an account? Login here");

        // Add all elements to container
        root.getChildren().addAll(
                titleLabel,
                emailField,
                usernameField,
                passwordField,
                confirmPasswordField,
                registerButton,
                loginLink
        );

        // Create scene
        scene = new Scene(root, 400, 600);
    }

    public Scene getScene() {
        return scene;
    }

    public Hyperlink getLoginLink() {
        return loginLink;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}



