package com.dh.roomly.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {



//    @GetMapping("/{username}")
//    public UserSaveOutput getUser(@PathVariable String username) {
//        log.info("Getting user with username: {}", username);
//        return userService.getUser(username);
//    }
}