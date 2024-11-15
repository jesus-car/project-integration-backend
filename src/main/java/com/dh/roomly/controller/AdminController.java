package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserGetDTOOutput;
import com.dh.roomly.dto.impl.UserUpdateRoleInput;
import com.dh.roomly.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @PatchMapping("/users/{id}/role")
    @Operation(summary = "Update user role")
    public String updateUserRole(@PathVariable Long id, @RequestBody UserUpdateRoleInput roles) {
        return userService.updateUserRole(id, roles);
    }

    @GetMapping("/users/all")
    public Page<UserGetDTOOutput> getAllUsers( @PageableDefault(size = 10, page = 0)Pageable pageable) {
        return userService.findAll(pageable);
    }
}
