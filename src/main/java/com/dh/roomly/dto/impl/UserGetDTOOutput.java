package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDTOOutput implements IDTOEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String identificationNumber;
    private String phoneNumber;
    private String city;
    private FileEntity profilePhoto;
    private LocalDateTime createdAt;
    private RoleEntity role;
}
