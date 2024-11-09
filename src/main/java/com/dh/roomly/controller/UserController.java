package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.UserPatchImgOutput;
import com.dh.roomly.exception.MissingImageException;
import com.dh.roomly.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;


    @PatchMapping(value = "/{id}/profile-picture" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserPatchImgOutput updateUserProfilePicture(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new MissingImageException("At least one non-empty image must be provided.");
        }
        return userService.updateUserProfileImage(id, image);
    }




//    @GetMapping("/{username}")
//    public UserSaveOutput getUser(@PathVariable String username) {
//        log.info("Getting user with username: {}", username);
//        return userService.getUser(username);
//    }
}