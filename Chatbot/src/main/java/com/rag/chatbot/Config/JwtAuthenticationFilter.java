package com.rag.chatbot.Config;

import com.rag.chatbot.Service.JWTService;
import com.rag.chatbot.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JWTService jwtService, @Lazy UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        // Log the URI and the Authorization header for debugging
        System.out.println("Request URI: " + uri);
        System.out.println("Authorization header: " + request.getHeader("Authorization"));

        // Skip JWT validation for /register and /login endpoints
        if (uri.startsWith("/api/auth/register") || uri.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);  // Allow the request to pass through
            return;
        }

        // Extract JWT Token from the Authorization header
        String token = extractTokenFromRequest(request);
        if (token != null) {
            System.out.println("Extracted token: " + token);
        }

        // If token exists, validate it and authenticate the user
        if (token != null && jwtService.isTokenValid(token)) {
            // Extract user details (username/email) from the token
            String username = jwtService.extractUsername(token);

            // Load user details from the UserService
            var userDetails = userService.loadUserByUsername(username);
            System.out.println("User details: " + userDetails);

            // Create an authentication token
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // Set the details for the authentication (can be used for things like IP address, etc.)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("Token is invalid or missing");
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Extract the token from the Authorization header
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Extract the token part
        }
        return null;
    }


}
