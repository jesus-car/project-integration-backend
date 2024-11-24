package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTOInput implements IDTOEntity {
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = Long.MAX_VALUE, message = Constants.NOT_GREATER_THAN_MAX_VALUE_LONG)
    @NotNull(message = Constants.NOT_NULL)
    @Schema(example = "1", description = "ID of the property, within the range of a Long.")
    private Long propertyId;
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = Long.MAX_VALUE, message = Constants.NOT_GREATER_THAN_MAX_VALUE_LONG)
    @NotNull(message = Constants.NOT_NULL)
    @Schema(example = "1", description = "ID of the user, within the range of a Long.")
    private Long userId;
    @NotNull(message = Constants.NOT_NULL)
    private String comment;
    @Min(value = 1, message = Constants.NOT_LESS_THAN_ONE)
    @Max(value = 5, message = Constants.NOT_GREATER_THAN_FIVE_BYTE)
    @Schema(example = "1", description = "Number of guest, not greater than 5")
    @NotNull(message = Constants.NOT_NULL)
    private Byte rating;
    private LocalDateTime date;
}
