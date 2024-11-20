package com.dh.roomly.service;

import com.dh.roomly.dto.impl.BookingDTOInput;
import com.dh.roomly.dto.impl.BookingDTOOutput;

public interface IBookingService {
    BookingDTOOutput create(BookingDTOInput bookingDTOInput);
}
