package com.rag.chatbot.DTO;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;

    // Manually add the constructor
    public JwtAuthenticationResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
