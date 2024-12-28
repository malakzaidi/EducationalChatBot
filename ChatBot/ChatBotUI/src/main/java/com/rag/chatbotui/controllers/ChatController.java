package com.rag.chatbotui.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ChatController {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:9090/api/auth/chat";
    private boolean isFirstMessage = true;

    public ChatController() {
        this.restTemplate = new RestTemplate();
    }

    public String sendMessage(String message) {
        // Handle greeting
        if (isGreeting(message.toLowerCase())) {
            isFirstMessage = false;
            return formatGreeting();
        }

        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/ask")
                .queryParam("request", message)
                .build()
                .encode()
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);

        if (response != null) {
            Document doc = Jsoup.parse(response);
            String cleanText = doc.text();

            // Format the response
            cleanText = formatResponse(cleanText);

            return cleanText.trim();
        }

        return "No response received";
    }

    private boolean isGreeting(String message) {
        return message.contains("salut") ||
                message.contains("bonjour") ||
                message.contains("hello") ||
                message.contains("hi");
    }

    private String formatGreeting() {
        StringBuilder greeting = new StringBuilder();
        greeting.append("Salut! 😊\n\n");
        greeting.append("Comment puis-je vous aider aujourd'hui? ");
        greeting.append("Je suis ChatEnset, un chatbot alimenté par llama 3.2, ");
        greeting.append("un expert en éducation universitaire spécialisé dans l'informatique notamment Java.");
        return greeting.toString();
    }

    private String formatResponse(String text) {
        // Add line breaks after periods followed by spaces
        text = text.replaceAll("\\. ", ".\n\n");

        // Add line breaks after question marks followed by spaces
        text = text.replaceAll("\\? ", "?\n\n");

        // Add extra spacing for readability around certain markers
        text = text.replaceAll(" \\* ", "\n\n• ");

        // Remove any excess blank lines
        text = text.replaceAll("\\n\\s*\\n\\s*\\n+", "\n\n");

        return text;
    }
}