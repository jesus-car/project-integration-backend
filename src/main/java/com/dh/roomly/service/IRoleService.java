package com.dh.roomly.service;

import com.dh.roomly.dto.impl.RoleDTOOutput;
import com.dh.roomly.entity.RoleEntity;

import java.util.List;

public interface IRoleService {
    List<RoleDTOOutput> findAll();
}
