package com.example.sqlconnect.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = Duration.ofDays(1).toMillis(); // 1 day in milliseconds

    public String generateToken(String subject, List<String> roles) {
        Instant now = Instant.now();

        // Generate the JWT token with roles
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles) // Add roles to the JWT payload
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(EXPIRATION_TIME)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

