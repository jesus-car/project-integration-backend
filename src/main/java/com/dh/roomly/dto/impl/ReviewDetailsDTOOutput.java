package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailsDTOOutput implements IDTOEntity {
    private Long propertyId;
    private Long userId;
    private String username;
    private String comment;
    private Byte rating;
    private LocalDateTime date;
}
