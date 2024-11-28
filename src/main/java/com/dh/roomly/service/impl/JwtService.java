package com.dh.roomly.service.impl;

import com.dh.roomly.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dh.roomly.common.JwtTokenConfig.SECRET_KEY;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String generateAccessToken(UserEntity user) {
        return generateToken(getExtraClaims(user), user, new Date(System.currentTimeMillis() + 3600000));
    }

    public String generateRefreshToken(UserEntity user) {
        return generateToken(getExtraClaims(user), user,  new Date(System.currentTimeMillis() + 3600000*24));
    }

    private Map<String, Object> getExtraClaims(UserEntity user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("email", user.getEmail());
        return extraClaims;
    }

    private String generateToken(Map<String, Object> extraClaims, UserEntity user, Date expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("email", String.class);
    }

    public boolean isRefreshTokenValid(String refreshToken, UserEntity user) {
        final String username = getUsernameFromToken(refreshToken);

        return (username.equals(user.getUsername()));
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
