package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserLoginInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginInput userLoginInput) {
        return ResponseEntity.ok("Login successful");
    }
}
