package com.dh.roomly.repository;

import com.dh.roomly.entity.BookingEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookingRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> findByUserId(Long id);
    List<BookingEntity> findByUserIdAndPropertyId(Long userId, Long propertyId);
}
