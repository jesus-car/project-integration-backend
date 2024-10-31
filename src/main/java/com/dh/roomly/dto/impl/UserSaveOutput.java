package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import com.dh.roomly.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveOutput implements IDTOEntity {
    private String firstName;
    private String lastName;
    private String identificationNumber;
    private String email;
    private String username;
    private String phoneNumber;
    private String city;
    private Long profilePhotoId;
    private LocalDateTime createdAt;
    private Set<Role> roles;
}