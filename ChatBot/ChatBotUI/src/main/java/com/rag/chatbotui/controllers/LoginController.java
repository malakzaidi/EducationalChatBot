package com.rag.chatbotui.controllers;

import com.rag.chatbot.DTO.AuthRequest;
import com.rag.chatbotui.JavaFxApplication;
import com.rag.chatbotui.scenes.ChatScene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoginController {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ChatScene chatScene;
    private Stage primaryStage;
    private final JavaFxApplication javaFxApplication;

    public LoginController(
            RestTemplate restTemplate,
            @Value("${api.base-url}") String baseUrl,
            @Lazy ChatScene chatScene,
            JavaFxApplication javaFxApplication
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.chatScene = chatScene;
        this.javaFxApplication = javaFxApplication;
    }

    public void navigateToRegister() {
        javaFxApplication.showRegisterScene();
    }

    public void navigateToLoginAdmin() {
        javaFxApplication.showLoginAdminScene();
    }


    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }


    public boolean handleLogin(String email, String password) {
        if (!isValidInput(email, password)) {
            return false;
        }

        try {

            AuthRequest request = new AuthRequest(email, password);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/login",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Login failed!");
                return false;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login failed: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidInput(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Email is required");
            return false;
        }

        if (password == null || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password is required");
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

}





