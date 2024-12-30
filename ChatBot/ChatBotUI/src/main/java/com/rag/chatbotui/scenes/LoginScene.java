package com.rag.chatbotui.scenes;

import com.rag.chatbotui.controllers.LoginController;
import com.rag.chatbotui.JavaFxApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.stereotype.Component;

@Component
public class LoginScene {
    private final LoginController loginController;
    private Scene scene;
    private TextField emailField;
    private PasswordField passwordField;
    private Label feedbackLabel;

    public LoginScene(LoginController loginController) {
        this.loginController = loginController;

        StackPane mainRoot = new StackPane();
        mainRoot.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #F0F8FF, #E6E6FA);"
        );

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setMaxWidth(350);

        Label titleLabel = new Label("Welcome Back");
        titleLabel.setFont(Font.font("Marcellus", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.valueOf("#2C3E50"));

        emailField = createStyledTextField("Email");
        passwordField = createStyledPasswordField("Password");

        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #4A90E2; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-family: 'Marcellus'; " +
                        "-fx-padding: 12px 16px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        loginButton.setPrefWidth(300);
        loginButton.setMaxWidth(300);
        loginButton.setOnAction(e -> handleLogin());

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register here");
        registerLink.setTextFill(Color.valueOf("#4A90E2"));
        registerLink.setOnAction(e -> loginController.navigateToRegister());

        // New Admin Login Hyperlink
        Hyperlink adminLoginLink = new Hyperlink("Login as an admin");
        adminLoginLink.setTextFill(Color.valueOf("#4A90E2"));
        adminLoginLink.setOnAction(e -> loginController.navigateToLoginAdmin());



        feedbackLabel = new Label();
        feedbackLabel.setTextFill(Color.RED);

        root.getChildren().addAll(titleLabel, emailField, passwordField, loginButton, registerLink, adminLoginLink, feedbackLabel);

        mainRoot.getChildren().add(root);
        scene = new Scene(mainRoot, 400, 500);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean isLoginSuccessful = loginController.handleLogin(email, password);

        if (isLoginSuccessful) {
            JavaFxApplication javaFxApp = new JavaFxApplication();
            javaFxApp.showChatScene();
        } else {
            feedbackLabel.setText("Invalid credentials, please try again.");
        }
    }

    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setPrefWidth(300);
        field.setMaxWidth(300);
        field.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #2C3E50; " +
                        "-fx-prompt-text-fill: #95A5A6; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-padding: 12px 16px; " +
                        "-fx-font-family: 'Marcellus'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 1);"
        );
        return field;
    }

    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        field.setPrefWidth(300);
        field.setMaxWidth(300);
        field.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #2C3E50; " +
                        "-fx-prompt-text-fill: #95A5A6; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-padding: 12px 16px; " +
                        "-fx-font-family: 'Marcellus'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 1);"
        );
        return field;
    }

    public Scene getScene() {
        return scene;
    }
}



