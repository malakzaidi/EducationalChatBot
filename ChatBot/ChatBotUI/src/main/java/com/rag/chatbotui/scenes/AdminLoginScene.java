package com.rag.chatbotui.scenes;

import com.rag.chatbotui.controllers.AdminLoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminLoginScene {

    private Scene scene;
    private final AdminLoginController adminLoginController;

    @Autowired
    public AdminLoginScene(AdminLoginController adminLoginController) {
        this.adminLoginController = adminLoginController;

        StackPane mainRoot = new StackPane();
        mainRoot.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #F0F8FF, #E6E6FA);"
        );

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(350);

        Label titleLabel = new Label("Admin Login");
        titleLabel.setFont(Font.font("Marcellus", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.valueOf("#2C3E50"));

        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");


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
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));


        Label feedbackLabel = new Label();
        feedbackLabel.setTextFill(Color.RED);

        layout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, feedbackLabel);

        mainRoot.getChildren().add(layout);
        scene = new Scene(mainRoot, 400, 500);
    }

    private void handleLogin(String username, String password) {
        adminLoginController.loginAdmin(username, password);
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
