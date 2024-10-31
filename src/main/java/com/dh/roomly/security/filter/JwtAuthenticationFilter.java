package com.dh.roomly.security.filter;

import com.dh.roomly.dto.impl.UserLoginInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static com.dh.roomly.common.JwtTokenConfig.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    protected static final Date DATE_EXPIRATION = new Date(System.currentTimeMillis() + 3600000);

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Asegurarse de que la solicitud es POST y que el Content-Type es application/json
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Método de autenticación no soportado: " + request.getMethod());
        }

        if (!request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Content-Type no soportado: " + request.getContentType());
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

        try {
            UserLoginInput userLoginInput = new ObjectMapper().readValue(request.getInputStream(), UserLoginInput.class);
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userLoginInput.getEmail(),
                    userLoginInput.getPassword()
            );
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid request");
        }

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String username = user.getUsername();

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(user.getAuthorities()))
                .subject(username)
                .build();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(DATE_EXPIRATION)
                .signWith(SECRET_KEY)
                .compact();

        Map<String,String> body = Map.of(
                "token", token,
                "username", username,
                "message", "User authenticated successfully"
        );

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String,String> body = Map.of(
                "message", "Authentication failed",
                "error", failed.getMessage()
        );

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
