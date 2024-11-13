package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureDTOInput implements IDTOEntity {
    @NotBlank(message = Constants.NOT_BLANK)
    @NotNull(message = Constants.NOT_NULL)
    @Size(max = 45, message = "Must be a maximum of 45 characters")
    private String name;
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    private String description;
    @NotBlank(message = Constants.NOT_BLANK)
    @NotNull(message = Constants.NOT_NULL)
    @Size(max = 45, message = "Must be a maximum of 45 characters")
    private String iconName;
}
