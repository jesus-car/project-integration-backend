package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTOInput implements IDTOEntity {

    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÑn]+( [A-Za-zÑñ]+)*$",
            message = "Must contain only letters and single spaces between words")
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    private String name;
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÑn]+( [A-Za-zÑñ]+)*$",
            message = "Must contain only letters and single spaces between words")
    @Size(max = 512, message = "Must be a maximum of 512 characters")
    private String description;
    private BigDecimal pricePerNight;
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short cityId;
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÑn\\-#]+( [A-Za-zÑñ\\-#]+)*$",
            message = "Must contain only letters and single spaces between words " +
                    "and special characters like - or #")
    @Size(max = 256, message = "Must be a maximum of 256 characters")
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
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = Long.MAX_VALUE, message = Constants.NOT_GREATER_THAN_MAX_VALUE_LONG)
    private Long ownerId;
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 255, message = Constants.NOT_GREATER_THAN_MAX_VALUE_SHORT)
    private Short categoryId;
}