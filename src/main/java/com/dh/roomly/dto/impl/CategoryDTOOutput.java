package com.dh.roomly.dto.impl;

import com.dh.roomly.common.Constants;
import com.dh.roomly.dto.IDTOEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTOOutput implements IDTOEntity {
    private Short id;
    @NotBlank(message = Constants.NOT_BLANK)
    @Size(max = 100, message = "Must be a maximum of 100 characters")
    private String title;
    @Size(max = 400, message = "Must be a maximum of 400 characters")
    private String description;
    private String imageUrl;
}