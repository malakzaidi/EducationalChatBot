package com.rag.chatbotui.controllers;

import com.rag.chatbot.DTO.JwtAuthenticationResponse;
import com.rag.chatbot.DTO.RegisterRequest;
import com.rag.chatbotui.JavaFxApplication;
import com.rag.chatbotui.scenes.RegisterScene;
import com.rag.chatbotui.service.AuthenticationService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JavaFxApplication javaFxApplication;

    // Reference to RegisterScene, which contains the UI elements
    private final RegisterScene registerScene;

    public RegisterController(RegisterScene registerScene) {
        this.registerScene = registerScene;
    }

    public void handleRegister() {
        // Get values from the input fields in the RegisterScene
        TextField emailField = (TextField) registerScene.getRoot().lookup("#emailField");
        TextField firstNameField = (TextField) registerScene.getRoot().lookup("#firstNameField");
        TextField lastNameField = (TextField) registerScene.getRoot().lookup("#lastNameField");
        TextField addressField = (TextField) registerScene.getRoot().lookup("#addressField");
        TextField phoneNumberField = (TextField) registerScene.getRoot().lookup("#phoneNumberField");
        PasswordField passwordField = (PasswordField) registerScene.getRoot().lookup("#passwordField");

        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Create RegisterRequest object
        RegisterRequest registerRequest = new RegisterRequest(email, password, firstName, lastName, address, phoneNumber);

        try {
            // Call the register method from the authentication service
            JwtAuthenticationResponse response = authenticationService.register(registerRequest);

            // Check if registration was successful
            if (response != null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("You can now login!");
                alert.showAndWait();

                // Redirect to login screen
                javaFxApplication.showLoginScene();
            } else {
                // If the response is null, show an error
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Registration Failed");
                alert.setHeaderText(null);
                alert.setContentText("Registration failed. Please try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Handle any exceptions during registration
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
}