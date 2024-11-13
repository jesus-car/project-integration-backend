package com.dh.roomly.repository;

import com.dh.roomly.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFeatureRepository extends JpaRepository<FeatureEntity, Short> {
    boolean existsByNameIgnoreCase(String name);
}
