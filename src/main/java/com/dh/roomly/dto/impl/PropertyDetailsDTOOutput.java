package com.dh.roomly.dto.impl;


import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import com.dh.roomly.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDetailsDTOOutput implements IDTOEntity {
    private Long id;

    @Schema(example = "Hermosa Villa", description = "Nombre de la propiedad. Solo letras con o sin tildes y espacios.")
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+( [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$",
            message = "Debe contener solo letras, incluyendo tildes, y espacios entre palabras")
    @Size(max = 100, message = "Debe tener un máximo de 100 caracteres")
    private String name;

    @Schema(example = "Una lujosa villa de 5 habitaciones con piscina privada", description = "Descripción de la propiedad. Puede contener letras, números, tildes y espacios.")
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9]+( [A-Za-zÁÉÍÓÚáéíóúÑñ0-9]+)*$",
            message = "Debe contener solo letras, números, tildes, y espacios entre palabras")
    @Size(max = 512, message = "Debe tener un máximo de 512 caracteres")
    private String description;

    private BigDecimal pricePerNight;

    private CityWithCountrySimpleDTO city;

    @Schema(example = "Calle Primavera 1234 #5", description = "Dirección exacta. Puede contener letras, números, tildes, espacios y caracteres especiales como - o #.")
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\-#]+( [A-Za-zÁÉÍÓÚáéíóúÑñ0-9\\-#]+)*$",
            message = "Debe contener solo letras, números, tildes, espacios, y caracteres especiales como - o #")
    @Size(max = 256, message = "Debe tener un máximo de 256 caracteres")
    private String exactAddress;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short maxCapacity;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short numRooms;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short numBeds;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short numBathrooms;

    private UserSimpleDTOOutput owner;

    private CategoryDTOOutput category;

    private String mainPhotoUrl;
    private List<String> photoUrls;
}