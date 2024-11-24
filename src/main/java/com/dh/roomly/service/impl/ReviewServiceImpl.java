package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.ReviewDTOInput;
import com.dh.roomly.dto.impl.ReviewDTOOutput;
import com.dh.roomly.entity.ReviewEntity;
import com.dh.roomly.entity.ReviewId;
import com.dh.roomly.repository.IReviewRepository;
import com.dh.roomly.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    IReviewRepository iReviewRepository;

    @Override
    public ReviewDTOOutput create(ReviewDTOInput reviewDTOInput) {
        reviewDTOInput.setDate(LocalDateTime.now());
        ReviewEntity reviewEntity = (ReviewEntity) MappingDTO.convertToEntity(reviewDTOInput, ReviewEntity.class);
        reviewEntity.setId(new ReviewId(reviewDTOInput.getUserId(), reviewDTOInput.getPropertyId()));
        ReviewEntity saved =  iReviewRepository.save(reviewEntity);
        ReviewDTOOutput reviewDTOOutput = (ReviewDTOOutput) MappingDTO.convertToDto(saved, new ReviewDTOOutput());
        reviewDTOOutput.setUserId(saved.getId().getUserId());
        reviewDTOOutput.setPropertyId(saved.getId().getPropertyId());
        return  reviewDTOOutput;
    }

}
