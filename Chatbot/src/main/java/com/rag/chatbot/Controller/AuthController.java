package com.rag.chatbot.Controller;

import com.rag.chatbot.DTO.AuthRequest;
import com.rag.chatbot.DTO.AuthResponse;
import com.rag.chatbot.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rag.chatbot.Service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userservice;

    // Constructor-based injection (Lombok will generate this for you with @RequiredArgsConstructor)
    public AuthController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userservice.register(request));
    }


}

