package com.dh.roomly.repository;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
