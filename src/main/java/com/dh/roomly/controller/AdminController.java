package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserGetDTOOutput;
import com.dh.roomly.dto.impl.UserUpdateRoleInput;
import com.dh.roomly.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @PatchMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable Long id, @RequestBody UserUpdateRoleInput roles) {
        return userService.updateUserRole(id, roles);
    }

    @GetMapping("/users/all")
    public List<UserGetDTOOutput> getAllUsers() {
        return userService.findAll();
    }
}
