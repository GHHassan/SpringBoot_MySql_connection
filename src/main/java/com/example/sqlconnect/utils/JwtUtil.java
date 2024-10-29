package com.example.sqlconnect.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.Duration;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = Duration.ofDays(1).toMillis(); // 1 day in milliseconds

    public String generateToken(String subject) {
        Instant now = Instant.now();

        // Generate the JWT token
        return Jwts.builder()
                .setSubject(subject) // Set the subject (typically the username or user ID)
                .setIssuedAt(Date.from(now)) // Set the issued date
                .setExpiration(Date.from(now.plusMillis(EXPIRATION_TIME))) // Set the expiration date
                .signWith(SECRET_KEY) // Sign the token with the secret key
                .compact(); // Compact the token into a string
    }
}

