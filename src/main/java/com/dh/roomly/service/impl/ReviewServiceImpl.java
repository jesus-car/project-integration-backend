package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.ReviewDTOInput;
import com.dh.roomly.dto.impl.ReviewDTOOutput;
import com.dh.roomly.entity.*;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.IBookingRepository;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.repository.IReviewRepository;
import com.dh.roomly.repository.IUserRepository;
import com.dh.roomly.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    IReviewRepository iReviewRepository;
    @Autowired
    IPropertyRepository propertyRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IBookingRepository bookingRepository;

    @Override
    public ReviewDTOOutput create(ReviewDTOInput reviewDTOInput) {
        propertyRepository.findById(String.valueOf(reviewDTOInput.getPropertyId()))
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + reviewDTOInput.getPropertyId()));
        userRepository.findById(reviewDTOInput.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + reviewDTOInput.getUserId()));
        List<BookingEntity> bookings = bookingRepository.findByUserIdAndPropertyId(reviewDTOInput.getUserId(), reviewDTOInput.getPropertyId());

        if (bookings.isEmpty()){
            throw new ResourceNotFoundException("No se encontro una reserva entre el usuario y propiedad proporcionados : "
                    + reviewDTOInput.getUserId() + ", " + reviewDTOInput.getPropertyId());
        }

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
