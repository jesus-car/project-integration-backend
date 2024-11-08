package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityWithCountrySimpleDTO implements IDTOEntity {
    private Short id;

    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$",
            message = "Debe contener solo letras, incluyendo tildes, y espacios entre palabras")
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    @Schema(example = "nombre ciudad", description = "Nombre de la ciudad. Puede contener letras, números, tildes, espacios y caracteres especiales como - o #.")
    private String name;

    private CountrySimpleDTO country;
}
