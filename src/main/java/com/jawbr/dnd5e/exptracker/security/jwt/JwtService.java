package com.jawbr.dnd5e.exptracker.security.jwt;

import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${security.jwt.expiration.minutes}")
    private int expiration;
    @Value("${security.jwt.secret.key}")
    private String key;

    public TokenDTO generateToken(String email) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MINUTE, expiration);
        Date expirationDate = currentDate.getTime();

        SecretKey secretKey = getSecretKey();

        String token = Jwts.builder()
                .claim("sub", email)
                .claim("exp", expirationDate.getTime() / 1000)
                .signWith(secretKey)
                .compact();

        long expirationDateInSeconds = TimeUnit.MILLISECONDS.toSeconds(expirationDate.getTime() - new Date().getTime());

        return TokenDTO.builder()
                .token(token)
                .expireIn(expirationDateInSeconds)
                .user(TokenDTO.UserDetailDTO.builder()
                        .email(email)
                        .build())
                .build();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return true;
    }
}
