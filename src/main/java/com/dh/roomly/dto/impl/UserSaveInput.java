package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.common.validation.IsValidEmail;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short cityId;
    @Email
    @NotBlank(message = Constants.NOT_BLANK)
    @IsValidEmail
    private String email;
    @NotBlank(message = Constants.NOT_BLANK)
    private String password;
}