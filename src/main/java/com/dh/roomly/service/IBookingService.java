package com.dh.roomly.service;

import com.dh.roomly.dto.impl.BookingDTOInput;
import com.dh.roomly.dto.impl.BookingDTOOutput;

import java.util.List;

public interface IBookingService {
    BookingDTOOutput create(BookingDTOInput bookingDTOInput);

    List<BookingDTOOutput> findByUserId(Long id);

}
