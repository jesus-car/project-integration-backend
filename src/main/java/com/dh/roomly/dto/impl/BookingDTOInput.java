package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import com.dh.roomly.entity.BookingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTOInput implements IDTOEntity {
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
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull(message = Constants.NOT_NULL)
    private BigDecimal totalPrice;
    @Min(value = 0, message = Constants.NOT_LESS_THAN_ZERO)
    @Max(value = 127, message = Constants.NOT_GREATER_THAN_MAX_VALUE_BYTE)
    @Schema(example = "1", description = "Number of guest, not greater than 127")
    @NotNull(message = Constants.NOT_NULL)
    private Byte numGuest;
    private BookingEntity.Status status;
    private LocalDateTime date;
    @AssertTrue(message = "startDate must not be greater than endDate")
    private boolean isStartDateBeforeOrEqualEndDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }
}
