package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTOInput implements IDTOEntity {
    @Schema(example = "Hermosa Villa", description = "Nombre de la propiedad.")
    @NotBlank(message = Constants.NOT_BLANK)
    @NotNull(message = Constants.NOT_NULL)
    @Size(max = 100, message = "Debe tener un máximo de 100 caracteres")
    private String name;

    @Schema(example = "Una lujosa villa de 5 habitaciones con piscina privada", description = "Descripción de la propiedad.")
    @Size(max = 512, message = "Debe tener un máximo de 512 caracteres")
    private String description;

    @NotNull(message = Constants.NOT_NULL)
    private BigDecimal pricePerNight;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @Schema(example = "1", description = "ID of the city, not greater than 255")
    @NotNull(message = Constants.NOT_NULL)
    private Short cityId;

    @Schema(example = "Calle Primavera 1234 #5", description = "Dirección exacta.")
    @NotBlank(message = Constants.NOT_BLANK)
    @NotNull(message = Constants.NOT_NULL)
    @Size(max = 256, message = "Debe tener un máximo de 256 caracteres")
    private String exactAddress;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @NotNull(message = Constants.NOT_NULL)
    private Short maxCapacity;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @NotNull(message = Constants.NOT_NULL)
    private Short numRooms;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @NotNull(message = Constants.NOT_NULL)
    @NotNull(message = Constants.NOT_NULL)
    private Short numBeds;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @NotNull(message = Constants.NOT_NULL)
    private Short numBathrooms;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = Long.MAX_VALUE, message = Constants.NOT_GREATER_THAN_MAX_VALUE_LONG)
    @NotNull(message = Constants.NOT_NULL)
    @Schema(example = "1", description = "ID of the owner, within the range of a Long.")
    private Long ownerId;

    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    @NotNull(message = Constants.NOT_NULL)
    @Schema(example = "1", description = "ID of the category, not greater than 255")
    private Short categoryId;

    @NotNull(message = Constants.NOT_NULL)
    @Size(min = 1, message = "Debe contener al menos un ID de feature.")
    @Schema(example = "[1,2]", description = "List of features ids, each one not greater than 255")
    private List<@Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
            Short> featureIds;
}
