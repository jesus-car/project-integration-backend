package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.RoleDTOOutput;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @GetMapping("/all")
    public List<RoleDTOOutput> getAllRoles() {
        return roleService.findAll();
    }
}
