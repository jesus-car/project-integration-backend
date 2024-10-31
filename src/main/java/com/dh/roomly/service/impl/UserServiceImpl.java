package com.dh.roomly.service.impl;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.impl.UserSaveInput;
import com.dh.roomly.dto.impl.UserSaveOutput;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.repository.RoleRepository;
import com.dh.roomly.repository.UserRepository;
import jakarta.persistence.Transient;
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
                .password(userSaveInput.getPassword())
                .email(userSaveInput.getEmail())
                .firstName(userSaveInput.getFirstName())
                .lastName(userSaveInput.getLastName())
                .identificationNumber(userSaveInput.getIdentificationNumber())
                .typeId(Short.parseShort(userSaveInput.getTypeId()))
                .phoneNumber(userSaveInput.getPhoneNumber())
                .cityId(userSaveInput.getCityId())
                .isEnabled(true)
                .isLocked(false)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        Set<RoleEntity> roleEntities = new HashSet<>();
        roleRepository.findByName(RoleEnum.ROLE_CLIENT).ifPresent(roleEntities::add);

        if (userEntity.isAdmin())
            roleRepository.findByName(RoleEnum.ROLE_ADMIN).ifPresent(roleEntities::add);

        if (userEntity.isSeller())
            roleRepository.findByName(RoleEnum.ROLE_SELLER).ifPresent(roleEntities::add);

        userEntity.setRoleEntities(roleEntities);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());

        UserEntity user = userRepository.save(userEntity);

        return UserSaveOutput.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCityId())
                .createdAt(user.getCreatedAt())
                .roleEntities(user.getRoleEntities())
                .build();
    }

    @Transactional
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    @Transactional(readOnly = true)
//    public UserEntity findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    @Transactional(readOnly = true)
//    public UserSaveOutput getUser(String username) {
//        UserEntity user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return UserSaveOutput.builder()
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .identificationNumber(user.getIdentificationNumber())
//                .phoneNumber(user.getPhoneNumber())
//                .city(user.getCity())
//                .createdAt(user.getCreatedAt())
//                .roleEntities(user.getRoleEntities())
//                .build();
//    }
}
