package com.rag.chatbotui.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

public class LoginScene {
    private Scene scene;
    private TextField usernameField;
    private PasswordField passwordField;
    private Hyperlink registerLink;
    private Button loginButton;

    public LoginScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1a237e, #4a148c);");

        Label titleLabel = new Label("Welcome Back");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        usernameField = createStyledTextField("Username");
        passwordField = createStyledPasswordField("Password");

        loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #00c853; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5;"
        );
        loginButton.setMaxWidth(300);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(loginButton.getStyle() + "-fx-background-color: #00e676;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(loginButton.getStyle() + "-fx-background-color: #00c853;"));

        registerLink = new Hyperlink("Don't have an account? Register here");
        registerLink.setTextFill(Color.LIGHTGRAY);

        root.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, registerLink);

        scene = new Scene(root, 400, 500);
        applyStyles(root);
    }

    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setMaxWidth(300);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: lightgray; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 10;"
        );
        return field;
    }

    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setMaxWidth(300);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: lightgray; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 10;"
        );
        return field;
    }

    private void applyStyles(VBox root) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setOffsetX(0);
        shadow.setOffsetY(2);
        shadow.setRadius(5);

        root.getChildren().forEach(node -> {
            if (node instanceof Control) {
                ((Control) node).setEffect(shadow);
            }
        });
    }

    public Scene getScene() {
        return scene;
    }

    public Hyperlink getRegisterLink() {
        return registerLink;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}



