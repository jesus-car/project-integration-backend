package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserGetDTOOutput;
import com.dh.roomly.dto.impl.UserUpdateRoleInput;
import com.dh.roomly.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @PatchMapping("/users/{id}/role")
    @Operation(summary = "Update user role")
    public String updateUserRole(@PathVariable Long id,@ParameterObject @RequestBody UserUpdateRoleInput roles) {
        return userService.updateUserRole(id, roles);
    }

    @GetMapping("/users/all")
    @Operation(summary = "Get all users with pagination")
    public Page<UserGetDTOOutput> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int page,
                                              @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size) {
        return userService.findAll(PageRequest.of(page, size));
    }
}
