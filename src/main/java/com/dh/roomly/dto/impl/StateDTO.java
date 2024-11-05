package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateDTO {
    private Short id;
    @NotBlank(message = Constants.NOT_BLANK)
    @Pattern(regexp = "^[A-Za-zÑn]+( [A-Za-zÑñ]+)*$",
            message = "Must contain only letters and single spaces between words")
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    private String name;
    private Set<CityDTO> cities;
}
