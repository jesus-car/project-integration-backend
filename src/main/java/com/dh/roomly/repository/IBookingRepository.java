package com.dh.roomly.repository;

import com.dh.roomly.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> findByUserId(Long id);
}
