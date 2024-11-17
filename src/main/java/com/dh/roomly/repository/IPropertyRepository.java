package com.dh.roomly.repository;

import com.dh.roomly.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPropertyRepository extends JpaRepository<PropertyEntity, String>, JpaSpecificationExecutor<PropertyEntity> {
    boolean existsByName(String name);

    @Query("SELECT p FROM PropertyEntity p JOIN FETCH p.owner WHERE p.id = :id")
    Optional<PropertyEntity> findByIdWithOwner(@Param("id") Long id);
    boolean existsByCategoryId(Short categoryId);
}
