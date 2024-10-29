package com.dh.roomly.dto.impl;

import com.dh.roomly.common.validation.IsValidUsername;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequest implements IDTOEntity {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @IsValidUsername
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}