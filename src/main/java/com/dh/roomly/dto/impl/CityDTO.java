package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO implements IDTOEntity {
    private Short id;
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÑn]+( [A-Za-zÑñ]+)*$",
            message = "Must contain only letters and single spaces between words")
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    private String name;
}
