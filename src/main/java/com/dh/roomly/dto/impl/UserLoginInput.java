package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginInput implements IDTOEntity {
    private String email;
    private String password;
}
