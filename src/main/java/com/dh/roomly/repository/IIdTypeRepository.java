package com.dh.roomly.repository;

import com.dh.roomly.entity.IdTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IIdTypeRepository extends JpaRepository<IdTypeEntity, Short> {
    Optional<IdTypeEntity> findById(Short id);
}
