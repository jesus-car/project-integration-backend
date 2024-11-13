package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureDTOOutput implements IDTOEntity {
    private Short id;
    private String name;
    private String description;
    private String iconName;
}
