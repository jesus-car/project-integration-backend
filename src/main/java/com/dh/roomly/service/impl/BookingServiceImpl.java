package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.BookingDTOInput;
import com.dh.roomly.dto.impl.BookingDTOOutput;
import com.dh.roomly.entity.BookingEntity;
import com.dh.roomly.repository.IBookingRepository;
import com.dh.roomly.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {
    @Autowired
    IBookingRepository iBookingRepository;

    @Override
    public BookingDTOOutput create(BookingDTOInput bookingDTOInput) {
        bookingDTOInput.setDate(LocalDateTime.now());
        bookingDTOInput.setStatus(BookingEntity.Status.CONFIRMED);
        BookingEntity saved =
                iBookingRepository.save(
                        (BookingEntity) MappingDTO.convertToEntity(bookingDTOInput, BookingEntity.class));
        BookingDTOOutput bookingDTOOutput = (BookingDTOOutput) MappingDTO.convertToDto(saved, new BookingDTOOutput());
        bookingDTOOutput.setPropertyId(saved.getPropertyId());
        bookingDTOOutput.setUserId(saved.getUserId());
        return  bookingDTOOutput;
    }

    @Override
    public List<BookingDTOOutput> findByUserId(Long id) {
        List<BookingEntity> bookingEntities = iBookingRepository.findByUserId(id);
        List<BookingDTOOutput> bookingDTOOutputs = new ArrayList<>();
        bookingEntities.forEach(bookingEntity -> {
            BookingDTOOutput bookingDTOOutput = (BookingDTOOutput) MappingDTO.convertToDto(bookingEntity, new BookingDTOOutput());
            bookingDTOOutput.setPropertyId(bookingEntity.getPropertyId());
            bookingDTOOutput.setUserId(bookingEntity.getUserId());
            bookingDTOOutputs.add(bookingDTOOutput);
        });
        return bookingDTOOutputs;
    }
}
