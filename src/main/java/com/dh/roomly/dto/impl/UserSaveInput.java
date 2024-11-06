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
public class UserSaveInput implements IDTOEntity {

    @NotBlank(message = Constants.NOT_BLANK)
    @Size(max = 256, message = "Must be a maximum of 256 characters")
    private String firstName;

    @NotBlank(message = Constants.NOT_BLANK)
    @Size(max = 256, message = "Must be a maximum of 256 characters")
    private String lastName;

    @NotNull(message = Constants.NOT_BLANK)
    private Long identificationNumber;

    @NotNull(message = Constants.NOT_BLANK)
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short typeId;

    @NotNull(message = Constants.NOT_BLANK)
    @Min(value = 10000000, message = "Ingrese un número de teléfono válido")
    private Integer phoneNumber;

    @NotNull(message = Constants.NOT_BLANK)
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short cityId;

    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Formato de email inválido"
    )
    @Size(max = 256, message = "Must be a maximum of 256 characters")
    @IsValidEmail
    private String email;
    @NotBlank(message = Constants.NOT_BLANK)
    @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
    private String password;
}