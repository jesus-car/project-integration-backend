package com.dh.roomly.service.impl;

import com.dh.roomly.common.Conflict;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.BookingDTOInput;
import com.dh.roomly.dto.impl.BookingDTOOutput;
import com.dh.roomly.entity.BookingEntity;
import com.dh.roomly.exception.ValidationException;
import com.dh.roomly.repository.IBookingRepository;
import com.dh.roomly.service.IBookingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingServiceImpl implements IBookingService {
    @Autowired
    IBookingRepository iBookingRepository;

    @Override
    public BookingDTOOutput create(BookingDTOInput bookingDTOInput) {
        validateBookingRange(bookingDTOInput.getPropertyId(), bookingDTOInput.getStartDate(), bookingDTOInput.getEndDate());
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
    @Transactional
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

    private void validateBookingRange(Long propertyId, LocalDate startDate, LocalDate endDate){
        List<BookingEntity> bookingEntities =
                this.iBookingRepository.findByStatusNotAndEndDateGreaterThanEqualAndPropertyId(
                        BookingEntity.Status.CANCELLED,
                        LocalDate.now(),
                        propertyId);
        LocalDate actualDate = LocalDate.now();
        if (actualDate.isAfter(startDate) || (!bookingEntities.isEmpty() && isInvalidDateRange(bookingEntities, startDate, endDate))){
            throw new ValidationException(Conflict.DATE_BOOKING_CONFLICT.toString());
        }
    }

    private boolean isInvalidDateRange(List<BookingEntity> bookingEntities, LocalDate start, LocalDate end){
        LocalDate minStartDate = bookingEntities.stream()
                .map(BookingEntity::getStartDate)
                .min(Comparator.naturalOrder())
                .orElse(null);

        LocalDate maxEndDate = bookingEntities.stream()
                .map(BookingEntity::getEndDate)
                .max(Comparator.naturalOrder())
                .orElse(null);

        assert minStartDate != null;
        LocalDate minDate = (minStartDate.isBefore(start)) ? minStartDate : start;
        LocalDate maxDate = (maxEndDate.isAfter(end)) ? maxEndDate : end;
        long rangeOfDays = ChronoUnit.DAYS.between(minDate, maxDate) + 1;
        int [] rangeArray = new int[(int) rangeOfDays + 1];
        bookingEntities.forEach(bookingEntity -> {
            long startIndex = ChronoUnit.DAYS.between(minDate, bookingEntity.getStartDate());
            long endIndex = ChronoUnit.DAYS.between(minDate, bookingEntity.getEndDate());
            for (int i = (int) startIndex; i <= (int) endIndex; i++) {
                rangeArray[i]++;
            }
        });

        long startIndex = ChronoUnit.DAYS.between(minDate, start);
        long endIndex = ChronoUnit.DAYS.between(minDate, end);
        for (int i = (int) startIndex; i <= (int) endIndex; i++) {
            rangeArray[i]++;
            if (rangeArray[i] > 1) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }
}
