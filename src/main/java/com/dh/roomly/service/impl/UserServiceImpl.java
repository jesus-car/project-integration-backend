package com.dh.roomly.service.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.common.JwtTokenConfig;
import com.dh.roomly.common.RoleEnum;
import com.dh.roomly.dto.impl.*;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.entity.RoleEntity;
import com.dh.roomly.entity.UserEntity;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.exception.UserNotAuthenticatedException;
import com.dh.roomly.repository.ICityRepository;
import com.dh.roomly.repository.IIdTypeRepository;
import com.dh.roomly.repository.IRoleRepository;
import com.dh.roomly.repository.IUserRepository;
import com.dh.roomly.service.IEmailService;
import com.dh.roomly.service.IFileService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.dh.roomly.common.Constants.ROLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final IRoleRepository roleRepository;
    private final ICityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IFileService fileService;
    private final IEmailService emailService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    private final IUserRepository userRepository;
    private final JwtService jwtService;
    private final IIdTypeRepository idTypeRepository;

    @Transactional
    public UserSaveDTOOutput register(UserSaveDTOInput userSaveDTOInput) {
        UserEntity userEntity = UserEntity.builder()
                .password(userSaveDTOInput.getPassword())
                .username(userSaveDTOInput.getEmail())
                .email(userSaveDTOInput.getEmail())
                .firstName(userSaveDTOInput.getFirstName())
                .lastName(userSaveDTOInput.getLastName())
                .identificationNumber(userSaveDTOInput.getIdentificationNumber())
                .phoneNumber(userSaveDTOInput.getPhoneNumber())
                .isEnabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();


        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(userSaveDTOInput.getPassword());

        if (decision.isCompromised()) {
            throw new IllegalArgumentException("Password is compromised");
        }
        // Set fields
        UserEntity user = setFieldsAndSaveUser(userSaveDTOInput, userEntity);

        emailService.sendEmail(user.getEmail(), "Welcome to Roomly", "Welcome to Roomly, " + user.getFirstName() + " " + user.getLastName() + "!");

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return UserSaveDTOOutput.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .identificationType(user.getTypeId().getName())
                .identificationNumber(user.getIdentificationNumber())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserEntity setFieldsAndSaveUser(UserSaveDTOInput userSaveDTOInput, UserEntity userEntity) {
        RoleEntity currentRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));


        if (userEntity.isSeller())
            currentRole = roleRepository.findByName(RoleEnum.ROLE_OWNER)
                    .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));


        if (userEntity.isAdmin())
            currentRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                    .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));


        userEntity.setRole(currentRole);

        // Encode password and set creation date
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());

        // Set city
        userEntity.setCity(cityRepository.findById(userSaveDTOInput.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found")));

        // Set Identification Type
        userEntity.setTypeId(idTypeRepository.findById(userSaveDTOInput.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Identification Type not found")));

        return userRepository.save(userEntity);
    }


    @Transactional
    public UserAuthDTOOutput login(UserAuthDTOInput userAuthDTOInput) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthDTOInput.getEmail(), userAuthDTOInput.getPassword()));

        UserEntity user = userRepository.findByEmail(userAuthDTOInput.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return UserAuthDTOOutput.builder()
                .token(accessToken)
                .message("Login successful")
                .refreshToken(refreshToken)
                .build();
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
                        .role(user.getRole())
                        .build());
    }

    @Transactional
    public String updateUserRole(Long id, UserUpdateRoleInput roles) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        RoleEntity newRole = roleRepository.findById(roles.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));

        user.setRole(newRole);
        userRepository.save(user);
        return "User role updated successfully";
    }

    public UserAuthDTOOutput refreshToken(
                HttpServletRequest request,
                HttpServletResponse response) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(JwtTokenConfig.PREFIX_TOKEN)) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String refreshToken = authHeader.substring(7);

        String username = jwtService.getUsernameFromToken(refreshToken);

        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String accessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        response.setHeader(HttpHeaders.AUTHORIZATION, JwtTokenConfig.PREFIX_TOKEN + newRefreshToken);

        return UserAuthDTOOutput.builder()
                .token(accessToken)
                .message("Token refreshed successfully")
                .refreshToken(newRefreshToken)
                .build();
    }

    public UserEntity getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UserNotAuthenticatedException("Token nulo o no comienza con 'Bearer'");
        }

        token = token.substring(7);

        try {
            // Extrae el nombre de usuario del token usando el servicio JWT
            String username = jwtService.getUsernameFromToken(token);

            // Busca al usuario en el repositorio
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotAuthenticatedException("Usuario no encontrado con el token proporcionado"));
        } catch (MalformedJwtException e) {
            throw new UserNotAuthenticatedException("El token está malformado: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new UserNotAuthenticatedException("El token ha expirado: " + e.getMessage());
        } catch (Exception e) {
            throw new UserNotAuthenticatedException("Error al procesar el token: " + e.getMessage());
        }
    }

    public UserEntity finUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));
    }
}
