package com.dh.roomly.common;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public final class JwtTokenConfig {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String CONTENT_TYPE = "application/json";
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    private JwtTokenConfig() {
    }
}
