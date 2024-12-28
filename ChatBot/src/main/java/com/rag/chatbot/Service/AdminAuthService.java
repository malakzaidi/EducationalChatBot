package com.rag.chatbot.Service;

import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password"; // Change to a secure password

    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
