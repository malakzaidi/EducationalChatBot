package com.rag.chatbotui.controllers;

import com.rag.chatbotui.scenes.PdfUploadScene;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
public class AdminLoginController {

    private final RestTemplate restTemplate;
    private final PdfUploadScene pdfUploadScene;
    private static final String ADMIN_LOGIN_URL = "http://localhost:9090/api/vector-store/admin-login";


    @Autowired
    public AdminLoginController(RestTemplate restTemplate, PdfUploadScene pdfUploadScene) {
        this.restTemplate = restTemplate;
        this.pdfUploadScene = pdfUploadScene;
    }


    public void loginAdmin(String username, String password) {
        String url = ADMIN_LOGIN_URL + "?username=" + username + "&password=" + password;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            String token = response.getBody();
            showSuccessMessage("Login Successful!");
            navigateToPdfUploadScene(token);
        } else {
            showErrorMessage("Login Failed", response.getBody());
        }
    }


    private void navigateToPdfUploadScene(String token) {
        Stage primaryStage = new Stage();

        // Get the PdfUploadScene with the token passed
        Scene pdfUploadScene = this.pdfUploadScene.createScene(token);

        primaryStage.setScene(pdfUploadScene);
        primaryStage.show();
    }


    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
