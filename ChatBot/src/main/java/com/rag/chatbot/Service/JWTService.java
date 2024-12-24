package com.rag.chatbot.Service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    // Generate the access token
    public String generateToken(UserDetails userDetails) {
        try {
            String token = Jwts
                    .builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 hours expiration
                    .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Error generating access token", e);
        }
    }

    // Generate refresh token
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            String refreshToken = Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 days expiration
                    .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                    .compact();
            return refreshToken;
        } catch (Exception e) {
            throw new RuntimeException("Error generating refresh token", e);
        }
    }

    // Get signing key
    private Key getSigninKey() {
        try {
            byte[] key = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(key);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding secret key", e);
        }
    }

    // Extract claim from token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            throw new RuntimeException("Error checking token expiration", e);
        }
    }

    // Check if token is valid (with UserDetails)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            boolean expired = isTokenExpired(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !expired);
            return isValid;
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the token is valid (without UserDetails)
    public boolean isTokenValid(String token) {
        try {
            boolean expired = isTokenExpired(token);
            return !expired;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to validate a refresh token
    public boolean isRefreshTokenValid(String refreshToken) {
        return isTokenValid(refreshToken);
    }
}
