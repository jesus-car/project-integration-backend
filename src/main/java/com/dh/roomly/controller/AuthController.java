package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserAuthDTOInput;
import com.dh.roomly.dto.impl.UserAuthDTOOutput;
import com.dh.roomly.dto.impl.UserSaveDTOInput;
import com.dh.roomly.dto.impl.UserSaveDTOOutput;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public UserAuthDTOOutput login(@Valid @RequestBody UserAuthDTOInput userAuthDTOInput) {
        return userService.login(userAuthDTOInput);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = " NO TOCAR NO TOCAR NO TOCAR NO TOCAR")
    public UserAuthDTOOutput refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return userService.refreshToken(request, response);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSaveDTOOutput register(@Valid @RequestBody UserSaveDTOInput userSaveDTOInput) {
        return userService.register(userSaveDTOInput);
    }
}
