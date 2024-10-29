package com.dh.roomly.dto.impl;

import com.dh.roomly.dto.IDTOEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest implements IDTOEntity {
    private String username;
    private String password;
}
