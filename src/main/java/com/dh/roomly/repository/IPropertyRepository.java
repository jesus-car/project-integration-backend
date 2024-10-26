package com.dh.roomly.repository;

import com.dh.roomly.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPropertyRepository extends JpaRepository<PropertyEntity, String>, JpaSpecificationExecutor<PropertyEntity> {
}
