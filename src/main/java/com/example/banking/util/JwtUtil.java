package com.example.banking.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "really-strong-secret-key";

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .claim("USER_ID", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("Invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return ((Number) claims.get("USER_ID")).longValue();
    }
}

