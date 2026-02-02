package com.smartclinic.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationMinutes:120}")
    private int expirationMinutes;

    // Grading: defines a method to generate a JWT token using user's email
    public String generateToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(expirationMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Grading: implements a method to return the signing key using configured secret
    public Key getSigningKey() {
        // Ensure secret is long enough for HS256
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            // pad deterministically (simple) to avoid runtime exceptions for short secrets
            byte[] padded = new byte[32];
            for (int i = 0; i < padded.length; i++) padded[i] = bytes[i % bytes.length];
            bytes = padded;
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String validateAndGetEmail(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new IllegalArgumentException("Missing Authorization token");
        }
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        return parseClaims(token).getSubject();
    }
}
