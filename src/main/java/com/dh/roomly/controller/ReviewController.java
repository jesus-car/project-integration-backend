package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.ReviewDTOInput;
import com.dh.roomly.dto.impl.ReviewDTOOutput;
import com.dh.roomly.service.IReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("v1/reviews")
public class ReviewController {

    @Autowired
    private IReviewService ReviewService;

    @PostMapping("/rate")
    public ResponseEntity<ReviewDTOOutput> save(@Valid @RequestBody ReviewDTOInput dto) {
        return ResponseEntity.ok(ReviewService.create(dto));
    }

}
