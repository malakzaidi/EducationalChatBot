package com.rag.chatbotui.service;

import com.rag.chatbot.DTO.AuthRequest;
import com.rag.chatbot.DTO.JwtAuthenticationResponse;
import com.rag.chatbot.DTO.RegisterRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final String BASE_URL = "http://localhost:8083/api/auth";
    private final RestTemplate restTemplate;

    public AuthenticationService() {
        this.restTemplate = new RestTemplate();
    }

    public JwtAuthenticationResponse login(String email, String password) throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity(
                BASE_URL + "/login",
                entity,
                JwtAuthenticationResponse.class
        );

        if (response.getBody() == null) {
            throw new Exception("Authentication failed");
        }

        return response.getBody();
    }

    public JwtAuthenticationResponse register(RegisterRequest registerRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity(
                BASE_URL + "/register",
                entity,
                JwtAuthenticationResponse.class
        );

        if (response.getBody() == null) {
            throw new Exception("Registration failed");
        }

        return response.getBody();
    }
}