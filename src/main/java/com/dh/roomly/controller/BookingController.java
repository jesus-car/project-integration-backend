package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.BookingDTOInput;
import com.dh.roomly.dto.impl.BookingDTOOutput;
import com.dh.roomly.service.IBookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/reserve")
    public ResponseEntity<BookingDTOOutput> save(@Valid @RequestBody BookingDTOInput dto) {
        return ResponseEntity.ok(bookingService.create(dto));
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<BookingDTOOutput>> getByUserId(@PathVariable("userId") Long id) {
        List<BookingDTOOutput> bookingDTOOutputList = bookingService.findByUserId(id);
        if (bookingDTOOutputList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookingDTOOutputList);
    }
}
