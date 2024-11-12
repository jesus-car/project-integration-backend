package com.dh.roomly.dto.impl;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchImgDTOOutput {
    String message;
    String imageUri;
}
