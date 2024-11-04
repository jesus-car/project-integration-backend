package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthInput implements IDTOEntity {
    private String email;
    private String password;
}
