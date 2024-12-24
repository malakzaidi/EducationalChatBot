package com.rag.chatbot.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthRequest {
    private String email;
    private String password;

    // No-args constructor (for JPA or deserialization)
    public AuthRequest() {}

    // All-args constructor
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter and Setter methods
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Builder class
    public static class Builder {
        private String email;
        private String password;

        // Setter methods for Builder class
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        // Build method to create an instance of AuthRequest
        public AuthRequest build() {
            return new AuthRequest(email, password);
        }
    }

    // Static method to initiate the builder
    public static Builder builder() {
        return new Builder();
    }
}
