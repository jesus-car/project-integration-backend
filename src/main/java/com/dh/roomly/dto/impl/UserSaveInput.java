package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
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
public class UserSaveInput implements IDTOEntity {
    @NotBlank(message = Constants.NOT_BLANK)
    private String firstName;
    @NotBlank(message = Constants.NOT_BLANK)
    private String lastName;
    @NotBlank(message = Constants.NOT_BLANK)
    private String identificationNumber;
    @NotBlank(message = Constants.NOT_BLANK)
    private String typeId;
    @NotBlank(message = Constants.NOT_BLANK)
    private String phoneNumber;
    @NotBlank(message = Constants.NOT_BLANK)
    private String city;

    @NotBlank(message = Constants.NOT_BLANK)
    @IsValidUsername
    private String username;
    @Email
    private String email;
    @NotBlank(message = Constants.NOT_BLANK)
    private String password;
}