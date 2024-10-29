package com.dh.roomly.controller;


import com.dh.roomly.dto.impl.UserSaveRequest;
import com.dh.roomly.dto.impl.UserSaveResponse;
import com.dh.roomly.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping("/register")
    public UserSaveResponse saveUser(@Valid @RequestBody UserSaveRequest userSaveRequest) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(userSaveRequest.getPassword());

        if (decision.isCompromised()) {
            throw new IllegalArgumentException("Password is compromised");
        }
        return userService.saveUser(userSaveRequest);
    }

    @GetMapping("/{username}")
    public UserSaveResponse getUser(@PathVariable String username) {
        log.info("Getting user with username: {}", username);
        return userService.getUser(username);
    }
}