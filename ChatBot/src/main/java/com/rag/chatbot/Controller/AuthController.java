package com.rag.chatbot.Controller;

import com.rag.chatbot.DTO.*;
import com.rag.chatbot.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
        System.out.println("AuthController initialized");
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("\n=== Registration Request Started ===");
        System.out.println("Received registration request: " + registerRequest);
        // Print all fields from registerRequest
        System.out.println("Email: " + registerRequest.getEmail());
        System.out.println("First Name: " + registerRequest.getFirstName());
        System.out.println("Last Name: " + registerRequest.getLastName());
        System.out.println("Phone: " + registerRequest.getPhoneNumber());
        System.out.println("Address: " + registerRequest.getAddress());
        try {
            System.out.println("Attempting to register user...");
            JwtAuthenticationResponse response = userService.register(registerRequest);
            System.out.println("Registration successful!");
            System.out.println("JWT Token generated: " + (response.getToken() != null ? "Yes" : "No"));
            System.out.println("Refresh Token generated: " + (response.getRefreshToken() != null ? "Yes" : "No"));
            return ResponseEntity.ok(response);
        }  catch (Exception e) {
            System.out.println("\n=== Registration Error ===");
            System.out.println("Error type: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            System.out.println("Stack trace:");
            e.printStackTrace(System.out);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new JwtAuthenticationResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody AuthRequest authRequest) {
        System.out.println("\n=== Login Request Started ===");
        System.out.println("Login attempt for email: " + authRequest.getEmail());

        try {
            JwtAuthenticationResponse response = userService.authenticate(authRequest);
            System.out.println("Login successful for user: " + authRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Login failed. Error: " + e.getMessage());
            e.printStackTrace(System.out);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtAuthenticationResponse("Error", "Authentication failed"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        System.out.println("\n=== Token Refresh Request Started ===");
        try {
            JwtAuthenticationResponse response = userService.refreshToken(refreshTokenRequest);
            if (response == null) {
                System.out.println("Token refresh failed - invalid token");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            System.out.println("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Token refresh failed. Error: " + e.getMessage());
            e.printStackTrace(System.out);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleException(Exception e) {
            System.out.println("\n=== Global Exception Handler ===");
            System.out.println("Caught exception: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace(System.out);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}