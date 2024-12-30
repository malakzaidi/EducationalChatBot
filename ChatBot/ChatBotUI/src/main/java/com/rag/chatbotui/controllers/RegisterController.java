package com.rag.chatbotui.controllers;

import com.rag.chatbot.DTO.RegisterRequest;
import com.rag.chatbotui.JavaFxApplication;
import javafx.scene.control.Alert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RegisterController {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final JavaFxApplication javaFxApplication;

    public RegisterController(
            RestTemplate restTemplate,
            @Value("${api.base-url}") String baseUrl,
            JavaFxApplication javaFxapplication
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.javaFxApplication = javaFxapplication;
    }

    public boolean handleRegistration(String email, String username, String password, String confirmPassword,
                                      String firstName, String lastName, String address, String phoneNumber) {
        if (!isValidInput(email, username, password, confirmPassword, firstName, lastName, address, phoneNumber)) {
            return false;
        }

        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setAddress(address);
        request.setPhoneNumber(phoneNumber);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RegisterRequest> entity = new HttpEntity<>(request, headers);

            String response = restTemplate.postForObject(
                    baseUrl + "/register",
                    entity,
                    String.class
            );
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
            javaFxApplication.showLoginScene();
            return true;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidInput(String email, String username, String password, String confirmPassword,
                                 String firstName, String lastName, String address, String phoneNumber) {
        if (email == null || email.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Email is required");
            return false;
        }

        if (username == null || username.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username is required");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is required");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match");
            return false;
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First name is required");
            return false;
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Last name is required");
            return false;
        }

        if (address == null || address.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Address is required");
            return false;
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone number is required");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void navigateToLogin() {
        javaFxApplication.showLoginScene();
    }
}