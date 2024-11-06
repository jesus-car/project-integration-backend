package com.dh.roomly.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingImageException extends RuntimeException {
    public MissingImageException(String message) {
        super(message);
    }
}
