package com.dh.roomly.service.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.impl.*;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.TokenEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICityRepository;
import com.dh.roomly.repository.RoleRepository;
import com.dh.roomly.repository.TokenRepository;
import com.dh.roomly.repository.UserRepository;
import com.dh.roomly.service.IEmailService;
import com.dh.roomly.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final IEmailService emailService;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Transactional
    public UserSaveDTOOutput register(UserSaveDTOInput userSaveDTOInput) {
        UserEntity userEntity = UserEntity.builder()
                .password(userSaveDTOInput.getPassword())
                .username(userSaveDTOInput.getEmail())
                .email(userSaveDTOInput.getEmail())
                .firstName(userSaveDTOInput.getFirstName())
                .lastName(userSaveDTOInput.getLastName())
                .identificationNumber(userSaveDTOInput.getIdentificationNumber())
                .typeId(userSaveDTOInput.getTypeId())
                .phoneNumber(userSaveDTOInput.getPhoneNumber())
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
        userEntity.setCity(cityRepository.findById(userSaveDTOInput.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found")));

        UserEntity user = userRepository.save(userEntity);

        emailService.sendEmail(user.getEmail(), "Welcome to Roomly", "Welcome to Roomly, " + user.getFirstName() + " " + user.getLastName() + "!");

        String jwt = saveUserToken(user);

        return UserSaveDTOOutput.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .roleEntities(user.getRoles())
                .token(jwt)
                .build();
    }


    @Transactional
    public UserAuthDTOOutput login(UserAuthDTOInput userAuthDTOInput) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthDTOInput.getEmail(), userAuthDTOInput.getPassword()));

        UserEntity user = userRepository.findByEmail(userAuthDTOInput.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        revokeAllTokenByUser(user);
        String jwt = saveUserToken(user);

        return UserAuthDTOOutput.builder()
                .token(jwt)
                .message("Login successful")
                .build();
    }

    public String userLogout(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token is null");
        }

        TokenEntity tokenEntity = tokenRepository.findByToken(token).orElse(null);
        if(tokenEntity != null) {
            tokenEntity.setLoggedOut(true);
            tokenRepository.save(tokenEntity);
        }

        return "Logout successful";
    }

    private void revokeAllTokenByUser(UserEntity user) {
        List<TokenEntity> validTokensListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if (!validTokensListByUser.isEmpty()) {
            validTokensListByUser.forEach(tokenEntity -> tokenEntity.setLoggedOut(true));
        }

        tokenRepository.saveAll(validTokensListByUser);
    }

    private String saveUserToken(UserEntity user) {
        String jwt = jwtService.getToken(user);
        TokenEntity tokenEntity = TokenEntity.builder()
                .token(jwt)
                .user(user)
                .loggedOut(false)
                .build();

        tokenRepository.save(tokenEntity);
        return jwt;
    }


    @Transactional
    public UserEntity findByEmail(String email) throws RuntimeException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
    }

    @Transactional
    public UserPatchImgDTOOutput updateUserProfileImage(Long id, MultipartFile image) throws IOException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        FileEntity fileEntity = uploadUserProfileImage(image);

        user.setProfilePhoto(fileEntity);
        userRepository.save(user);

        return UserPatchImgDTOOutput.builder()
                .message("Profile image updated successfully")
                .imageUri(user.getProfilePhoto().getUrl())
                .build();
    }

    private FileEntity uploadUserProfileImage(MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }

    @Transactional(readOnly = true)
    public Page<UserGetDTOOutput> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
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
                        .build());
    }

    @Transactional
    public String updateUserRole(Long id, UserUpdateRoleInput roles) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

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
