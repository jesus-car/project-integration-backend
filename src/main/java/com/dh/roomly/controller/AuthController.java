package com.dh.roomly.controller;

import com.dh.roomly.common.JwtTokenConfig;
import com.dh.roomly.dto.impl.UserAuthDTOInput;
import com.dh.roomly.dto.impl.UserAuthDTOOutput;
import com.dh.roomly.dto.impl.UserSaveDTOInput;
import com.dh.roomly.dto.impl.UserSaveDTOOutput;
import com.dh.roomly.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping("/login")
    public UserAuthDTOOutput login(@Valid @RequestBody UserAuthDTOInput userAuthDTOInput) {
        return userService.login(userAuthDTOInput);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String token = JwtTokenConfig.extractTokenFromHeader(request);
        return userService.userLogout(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSaveDTOOutput register(@Valid @RequestBody UserSaveDTOInput userSaveDTOInput) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(userSaveDTOInput.getPassword());

        if (decision.isCompromised()) {
            throw new IllegalArgumentException("Password is compromised");
        }
        return userService.register(userSaveDTOInput);
    }
}
