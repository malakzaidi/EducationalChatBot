package com.rag.chatbotui.controllers;

import com.rag.chatbot.DTO.JwtAuthenticationResponse;
import com.rag.chatbotui.JavaFxApplication;
import com.rag.chatbotui.service.AuthenticationService;
import com.rag.chatbotui.scenes.LoginScene;
import com.rag.chatbotui.scenes.RegisterScene;
import com.rag.chatbotui.utils.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Component;
import javafx.scene.paint.Color; // For Color class
import com.rag.chatbotui.scenes.ChatScene; // For MainScene class



@Component
public class LoginController {

    private final AuthenticationService authenticationService;
    private final LoginScene loginScene;
    private final RegisterScene registerScene;
    private final ChatScene chatScene;

    public LoginController(AuthenticationService authenticationService, LoginScene loginScene, RegisterScene registerScene, ChatScene chatScene) {
        this.authenticationService = authenticationService;
        this.loginScene = loginScene;
        this.registerScene = registerScene;
        this.chatScene = chatScene;
    }

    private boolean validateCredentials(String username, String password) {
        return "admin".equals(username) && "password".equals(password);
    }



    public void switchToRegisterScene(ActionEvent event) {
        SceneManager.switchScene(registerScene);
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }


}
