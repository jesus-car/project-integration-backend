package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserAuthInput;
import com.dh.roomly.dto.impl.UserAuthOutput;
import com.dh.roomly.dto.impl.UserSaveInput;
import com.dh.roomly.dto.impl.UserSaveOutput;
import com.dh.roomly.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping("/login")
    public UserAuthOutput login(@RequestBody UserAuthInput userAuthInput) {
        return userService.login(userAuthInput);
    }

    @PostMapping("/register")
    public UserSaveOutput register(@Valid @RequestBody UserSaveInput userSaveInput) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(userSaveInput.getPassword());

        if (decision.isCompromised()) {
            throw new IllegalArgumentException("Password is compromised");
        }
        return userService.register(userSaveInput);
    }
}
