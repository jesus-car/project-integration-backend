package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.common.validation.IsValidEmail;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveDTOInput implements IDTOEntity {

    @NotBlank(message = Constants.NOT_BLANK)
    @Size(max = 45, message = "Must be a maximum of 256 characters")
    private String firstName;

    @NotBlank(message = Constants.NOT_BLANK)
    @Size(max = 45, message = "Must be a maximum of 256 characters")
    private String lastName;

    @NotNull(message = Constants.NOT_BLANK)
    @Size(max = 25, message = "Must be a maximum of 45 characters")
    @Pattern(
            regexp = "^\\d+$",
            message = "Solo se permiten números"
    )
    private String identificationNumber;

    @NotNull(message = Constants.NOT_BLANK)
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short typeId;

    @NotNull(message = Constants.NOT_BLANK)
    @Size(max = 45, message = "Must be a maximum of 45 characters")
    @Pattern(
            regexp = "^\\d+$",
            message = "Solo se permiten números"
    )
    private String phoneNumber;

    @NotNull(message = Constants.NOT_BLANK)
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short cityId;

    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Formato de email inválido"
    )
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    @IsValidEmail
    private String email;
    @NotBlank(message = Constants.NOT_BLANK)
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#@$!%*?(_+=){}&|^.`'-])[A-Za-z\\d#@$!%*?(_+=){}&|^.`'-]{8,}$",
            message = "La contraseña debe tener al menos una letra mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;
}