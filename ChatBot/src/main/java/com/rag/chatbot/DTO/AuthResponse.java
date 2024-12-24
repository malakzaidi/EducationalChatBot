package com.rag.chatbot.DTO;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;

    // If you don't want to use the @AllArgsConstructor, you can manually add a constructor like this:
    public AuthResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}


