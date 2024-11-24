package com.dh.roomly.service;

import com.dh.roomly.dto.impl.ReviewDTOInput;
import com.dh.roomly.dto.impl.ReviewDTOOutput;

public interface IReviewService {
    ReviewDTOOutput create(ReviewDTOInput bookingDTOInput);
}
