package com.dh.roomly.repository;

import com.dh.roomly.entity.ReviewEntity;
import com.dh.roomly.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewEntity, ReviewId> {
}
