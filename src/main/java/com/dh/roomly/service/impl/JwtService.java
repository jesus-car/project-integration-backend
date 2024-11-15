package com.dh.roomly.service.impl;

import com.dh.roomly.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.dh.roomly.common.JwtTokenConfig.SECRET_KEY;

@Service
@RequiredArgsConstructor
public class JwtService {


    protected static final Date DATE_EXPIRATION = new Date(System.currentTimeMillis() + 3600000);
    protected static final Date DATE_EXPIRATION_REFRESH = new Date(System.currentTimeMillis() + 3600000*24);


    public String generateAccessToken(UserEntity user) {
        return generateToken(getExtraClaims(user), user, DATE_EXPIRATION);
    }

    public String generateRefreshToken(UserEntity user) {
        return generateToken(getExtraClaims(user), user, DATE_EXPIRATION_REFRESH);
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
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public boolean isRefreshTokenValid(String refreshToken, UserEntity user) {
        return (user.getId().toString().equals(getClaim(refreshToken, Claims::getSubject)) && !isTokenExpired(refreshToken));
    }
}
