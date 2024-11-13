package com.dh.roomly.service.impl;

import com.dh.roomly.dto.impl.RoleDTOOutput;
import com.dh.roomly.repository.IRoleRepository;
import com.dh.roomly.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Override
    public List<RoleDTOOutput> findAll() {
        return roleRepository.findAll().stream()
                .map(roleEntity -> RoleDTOOutput.builder()
                        .id(roleEntity.getId())
                        .name(roleEntity.getName().toString())
                        .description(roleEntity.getDescription())
                        .build())
                .toList();
    }
}
