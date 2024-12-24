package com.rag.chatbot.Service;

import com.rag.chatbot.DTO.JwtAuthenticationResponse;
import com.rag.chatbot.DTO.RefreshTokenRequest;
import com.rag.chatbot.DTO.RegisterRequest;
import com.rag.chatbot.DTO.AuthRequest;
import com.rag.chatbot.Entity.Role;
import com.rag.chatbot.Entity.User;
import com.rag.chatbot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy AuthenticationManager authenticationManager,
                       JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    // Override the method from UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user from the repository by email (which is the username in this case)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user;  // Return the User entity which implements UserDetails
    }

    // Register a new user and return JWT tokens
    public JwtAuthenticationResponse register(RegisterRequest registerRequest) {
        // Create a new User object
        User user = new User();
        user.setFirstname(registerRequest.getFirstName());
        user.setLastname(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhoneNumber());
        user.setAddress(registerRequest.getAddress());
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER); // Default role

        // Encode the password
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);

        // Save the user to the repository
        userRepository.save(user);

        // Generate JWT and refresh tokens
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        // Return JWT response with tokens
        return new JwtAuthenticationResponse(jwt, refreshToken);

    }

    // Sign-in (authenticate) the user and return JWT tokens
    public JwtAuthenticationResponse authenticate (AuthRequest authRequest) {
        // Fetch user from the database
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Authenticate the user
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // Generate JWT and refresh tokens
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        // Return JWT response with tokens
        return new JwtAuthenticationResponse(jwt, refreshToken);
    }

    // Refresh the JWT token using a refresh token
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // Extract the username (email) from the refresh token
        String email = jwtService.extractUsername(refreshTokenRequest.getToken());

        // Fetch the user from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the refresh token is valid
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            // Generate a new JWT token
            String jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt, refreshTokenRequest.getToken());
        }

        // Return null if the refresh token is invalid
        return null;
    }

    // Save user details manually (optional)
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
