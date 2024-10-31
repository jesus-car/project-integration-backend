package com.dh.roomly.service.impl;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.impl.UserSaveInput;
import com.dh.roomly.dto.impl.UserSaveOutput;
import com.dh.roomly.entity.Role;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.repository.RoleRepository;
import com.dh.roomly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    public UserSaveOutput saveUser(UserSaveInput userSaveInput) {
        UserEntity userEntity = UserEntity.builder()
                .username(userSaveInput.getUsername())
                .password(userSaveInput.getPassword())
                .email(userSaveInput.getEmail())
                .firstName(userSaveInput.getFirstName())
                .lastName(userSaveInput.getLastName())
                .identificationNumber(userSaveInput.getIdentificationNumber())
                .typeId(Short.parseShort(userSaveInput.getTypeId()))
                .phoneNumber(userSaveInput.getPhoneNumber())
                .city(userSaveInput.getCity())
                .isEnabled(true)
                .isLocked(false)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        Set<Role> roles = new HashSet<>();
        roleRepository.findByName(RoleEnum.ROLE_USER).ifPresent(roles::add);

        if (userEntity.isAdmin())
            roleRepository.findByName(RoleEnum.ROLE_ADMIN).ifPresent(roles::add);

        userEntity.setRoles(roles);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());

        UserEntity user = userRepository.save(userEntity);

        return UserSaveOutput.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }

    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserSaveOutput getUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserSaveOutput.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }
}
