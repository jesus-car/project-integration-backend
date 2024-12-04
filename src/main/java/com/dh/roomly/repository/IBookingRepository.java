package com.dh.roomly.repository;

import com.dh.roomly.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<BookingEntity,Long> {
    List<BookingEntity> findByUserId(Long id);
    List<BookingEntity> findByUserIdAndPropertyId(Long userId, Long propertyId);
    List<BookingEntity> findByStatusNotAndEndDateGreaterThanEqualAndPropertyId(
            BookingEntity.Status status,
            LocalDate endDate,
            Long propertyId
    );
}
