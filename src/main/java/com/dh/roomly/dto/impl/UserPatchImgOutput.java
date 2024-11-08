package com.dh.roomly.dto.impl;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchImgOutput {
    String message;
    String imageUri;
}
