package com.dh.roomly.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ErrorDetailsDTO {
    private LocalDateTime timestamp;
    private String message;
    private List<String> details;
}