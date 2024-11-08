package com.dh.roomly.service.impl;

import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.*;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICityRepository;
import com.dh.roomly.repository.RoleRepository;
import com.dh.roomly.repository.UserRepository;
import com.dh.roomly.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final RoleRepository roleRepository;
    private final ICityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IFileService fileService;

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
                .typeId(userSaveInput.getTypeId())
                .phoneNumber(userSaveInput.getPhoneNumber())
                .isEnabled(true)
                .isLocked(false)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();

        // Set roles
        Set<RoleEntity> roleEntities = new HashSet<>();
        roleRepository.findByName(RoleEnum.ROLE_CLIENT).ifPresent(roleEntities::add);

        if (userEntity.isAdmin())
            roleRepository.findByName(RoleEnum.ROLE_ADMIN).ifPresent(roleEntities::add);

        if (userEntity.isSeller())
            roleRepository.findByName(RoleEnum.ROLE_SELLER).ifPresent(roleEntities::add);

        userEntity.setRoles(roleEntities);

        // Encode password and set creation date
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());

        // Set city
        userEntity.setCity(cityRepository.findById(userSaveInput.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found")));

        UserEntity user = userRepository.save(userEntity);

        return UserSaveOutput.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .roleEntities(user.getRoles())
                .token(jwtService.getToken(user))
                .build();
    }

    @Transactional
    public UserAuthOutput login(UserAuthInput userAuthInput) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthInput.getEmail(), userAuthInput.getPassword()));

        UserDetails user = userRepository.findByEmail(userAuthInput.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.getToken(user);

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

    @Transactional
    public UserPatchImgOutput updateUserProfileImage(Long id, MultipartFile image) throws IOException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FileEntity fileEntity = uploadUserProfileImage(image);

        user.setProfilePhoto(fileEntity);
        userRepository.save(user);

        return UserPatchImgOutput.builder()
                .message("Profile image updated successfully")
                .imageUri(user.getProfilePhoto().getUrl())
                .build();
    }

    private FileEntity uploadUserProfileImage(MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @Transactional(readOnly = true)
    public List<UserGetDTOOutput> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserGetDTOOutput.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .identificationNumber(user.getIdentificationNumber())
                        .phoneNumber(user.getPhoneNumber())
                        .city(user.getCity().getName())
                        .profilePhoto(user.getProfilePhoto())
                        .createdAt(user.getCreatedAt())
                        .roleEntities(user.getRoles())
                        .build())
                .toList();
    }

    @Transactional
    public String updateUserRole(Long id, UserUpdateRoleInput roles) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<RoleEntity> roleEntities = new HashSet<>();
        roles.getRoles().forEach(role -> {
            RoleEntity localRole = roleRepository.findById(role).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            roleEntities.add(localRole);
        });

        user.setRoles(roleEntities);
        userRepository.save(user);
        return "User role updated successfully";
    }
}
