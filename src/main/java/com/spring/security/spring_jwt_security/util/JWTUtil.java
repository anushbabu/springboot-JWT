package com.spring.security.spring_jwt_security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private final String SECRET = "my-super-secret-key-that-is-long-enough-1234567890!@#";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60; //1hour


    public String generateToken(String username) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        } else {
            // Implement token generation logic here
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "ADMIN"); // Add any additional claims if needed
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new java.util.Date())
                    .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .addClaims(claims)
                    .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
                    .compact();
        }

    }

    public Claims extractAllClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claimsJws.getBody();

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public String extractusername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String username = claims.getSubject();
            System.out.println("Username extracted from token: " + username);
            return username;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public Date extractExpiration(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
