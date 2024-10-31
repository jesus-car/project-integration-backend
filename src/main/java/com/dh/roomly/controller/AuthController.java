package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserAuthInput;
import com.dh.roomly.dto.impl.UserAuthOutput;
import com.dh.roomly.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public UserAuthOutput login(@RequestBody UserAuthInput userAuthInput) {
        return userService.login(userAuthInput);
    }
}
