package com.jawbr.dnd5e.exptracker.config.security.jwt;

import com.jawbr.dnd5e.exptracker.dto.response.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${security.jwt.expiration.minutes}")
    private int expiration;
    @Value("${security.jwt.secret.key}")
    private String key;

    private final StringRedisTemplate redisTemplate;

    public JwtService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public TokenDTO generateToken(String email) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MINUTE, expiration);
        Date expirationDate = currentDate.getTime();

        SecretKey secretKey = getSecretKey();

        String token = Jwts.builder()
                .claim("sub", email)
                .claim("exp", expirationDate.getTime() / 1000)
                .id(UUID.randomUUID().toString())
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

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return true;
    }

    public void blacklistToken(String jti, long expirationMillis) {
        redisTemplate.opsForValue().set(jti, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String jti) {
        return redisTemplate.hasKey(jti);
    }
}
