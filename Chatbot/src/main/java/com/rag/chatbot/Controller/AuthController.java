package com.rag.chatbot.Controller;

import com.rag.chatbot.DTO.*;
import com.rag.chatbot.Entity.User;
import com.rag.chatbot.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        JwtAuthenticationResponse response = userService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody AuthRequest authRequest) {
        System.out.println("Received Login Request: " + authRequest);
        JwtAuthenticationResponse response = userService.authenticate(authRequest);
        return ResponseEntity.ok(response);
    }
    // Endpoint to handle refresh token requests
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtAuthenticationResponse response = userService.refreshToken(refreshTokenRequest);
        if (response == null) {
            return ResponseEntity.status(403).build();  // Forbidden if refresh fails
        }
        return ResponseEntity.ok(response);
    }

    // Endpoint to save a user manually (if needed)
    @PostMapping("/save")
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().build();  // Return status 200 OK
    }
}

