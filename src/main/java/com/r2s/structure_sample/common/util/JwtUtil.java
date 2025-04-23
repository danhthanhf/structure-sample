package com.r2s.structure_sample.common.util;

import com.r2s.structure_sample.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey;

    @Value("${app.jwt.expiredAt}")
    private long expiredAt;

    public JwtUtil() {
        this.secretKey = generateSecretKey();
    }

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredAt))
                .signWith(key)
                .claims()
                .and().compact();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isExpiredToken(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isValidToken(String token, String userName) {
        return extractAllClaims(token).getSubject().equals(userName) && !isExpiredToken(token);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token).getPayload();
    }


    private Key genrateKey() {
        byte bytes[] = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey() {
        byte bytes[] = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
