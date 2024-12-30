package com.rag.chatbotui.scenes;

import com.rag.chatbotui.controllers.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.stereotype.Component;

@Component
public class RegisterScene {
    private Scene scene;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField emailField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField addressField;
    private TextField phoneNumberField;
    private Hyperlink loginLink;
    private Button registerButton;
    private final RegisterController registerController;

    public RegisterScene(RegisterController registerController) {
        this.registerController = registerController;

        StackPane mainRoot = new StackPane();
        mainRoot.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #FFFFFF, #F0F8FF, #E6E6FA);"
        );

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setMaxWidth(350);

        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Marcellus", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2C3E50;");

        emailField = createStyledTextField("Email");
        firstNameField = createStyledTextField("First Name");
        lastNameField = createStyledTextField("Last Name");
        addressField = createStyledTextField("Address");
        phoneNumberField = createStyledTextField("Phone Number");
        usernameField = createStyledTextField("Username");
        passwordField = createStyledPasswordField("Password");
        confirmPasswordField = createStyledPasswordField("Confirm Password");

        registerButton = new Button("Register");
        registerButton.setStyle(
                "-fx-background-color: #4A90E2; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-family: 'Marcellus'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 12px 16px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        registerButton.setPrefWidth(300);
        registerButton.setMaxWidth(300);

        registerButton.setOnAction(e -> {
            boolean isRegistrationSuccessful = registerController.handleRegistration(
                    emailField.getText(),
                    usernameField.getText(),
                    passwordField.getText(),
                    confirmPasswordField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    phoneNumberField.getText()
            );

            if (isRegistrationSuccessful) {
                registerController.navigateToLogin();
            }
        });

        loginLink = new Hyperlink("Already have an account? Login here");
        loginLink.setStyle("-fx-text-fill: #4A90E2; -fx-font-family: 'Marcellus';");
        loginLink.setOnAction(e -> registerController.navigateToLogin());

        root.getChildren().addAll(
                titleLabel,
                emailField,
                firstNameField,
                lastNameField,
                addressField,
                phoneNumberField,
                usernameField,
                passwordField,
                confirmPasswordField,
                registerButton,
                loginLink
        );

        mainRoot.getChildren().add(root);
        scene = new Scene(mainRoot, 400, 600);
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

    public Hyperlink getLoginLink() {
        return loginLink;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}





