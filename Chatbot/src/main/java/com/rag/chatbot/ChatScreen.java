package com.rag.chatbot;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ChatScreen extends BorderPane {

    private ChatbotUI application;

    public ChatScreen(ChatbotUI application) {
        this.application = application;

        getStyleClass().add("chat-screen");

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.getStyleClass().add("chat-area");

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.getStyleClass().add("input-field");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("button");
        sendButton.setOnAction(e -> sendMessage(inputField, chatArea));

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button");
        logoutButton.setOnAction(e -> application.showLoginScreen());

        setTop(logoutButton);
        setCenter(chatArea);
        setBottom(inputBox);
    }

    private void sendMessage(TextField inputField, TextArea chatArea) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.appendText("You: " + message + "\n");
            // Here you would typically send the message to your Spring Boot backend
            // and then display the response
            chatArea.appendText("Bot: Thanks for your message!\n");
            inputField.clear();
        }
    }
}

