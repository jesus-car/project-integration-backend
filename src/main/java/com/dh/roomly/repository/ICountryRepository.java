package com.dh.roomly.repository;

import com.dh.roomly.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICountryRepository extends JpaRepository<CountryEntity,Short> {
    @Query("SELECT c FROM CountryEntity c LEFT JOIN FETCH c.states s LEFT JOIN FETCH s.cities")
    List<CountryEntity> findAllWithStatesAndCities();
    @Query("SELECT c FROM CountryEntity c LEFT JOIN FETCH c.states s LEFT JOIN FETCH s.cities WHERE c.id = :id")
    Optional<CountryEntity> findByIdWithStatesAndCities(Short id);
}
