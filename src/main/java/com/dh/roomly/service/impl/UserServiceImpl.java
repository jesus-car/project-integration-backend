package com.dh.roomly.service.impl;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.impl.UserAuthInput;
import com.dh.roomly.dto.impl.UserAuthOutput;
import com.dh.roomly.dto.impl.UserSaveInput;
import com.dh.roomly.dto.impl.UserSaveOutput;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.repository.RoleRepository;
import com.dh.roomly.repository.UserRepository;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public UserSaveOutput register(UserSaveInput userSaveInput) {
        UserEntity userEntity = UserEntity.builder()
                .password(userSaveInput.getPassword())
                .username(userSaveInput.getEmail())
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
                .accountNonLocked(true)
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
    public UserAuthOutput login(UserAuthInput userAuthInput){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthInput.getEmail(), userAuthInput.getPassword()));
        UserDetails userDetails = userRepository.findByEmail(userAuthInput.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.getToken(userDetails);
        return UserAuthOutput.builder()
                .token(token)
                .message("Login successful")
                .build();
    }

    @Transactional
    public UserEntity findByEmail(String email) throws RuntimeException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
