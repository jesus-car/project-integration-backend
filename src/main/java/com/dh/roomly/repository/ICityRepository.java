package com.dh.roomly.repository;


import com.dh.roomly.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICityRepository extends JpaRepository<CityEntity, Short> {
    List<CityEntity> findByStateId(Short stateId);
}
