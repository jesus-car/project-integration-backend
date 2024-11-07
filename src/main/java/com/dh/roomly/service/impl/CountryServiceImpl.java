package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.CountryWithCityDTO;
import com.dh.roomly.entity.CountryEntity;
import com.dh.roomly.repository.ICountryRepository;
import com.dh.roomly.service.ICountryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements ICountryService {

    private ICountryRepository countryRepository;

    public CountryWithCityDTO findById(Short id) {
        CountryEntity country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
        return (CountryWithCityDTO) MappingDTO.convertToDto(country, new CountryWithCityDTO());
    }

    @Override
    public List<CountryWithCityDTO> findAll() {
        List<CountryEntity> countries = countryRepository.findAllWithCities();
        return countries.stream()
                .map(country -> (CountryWithCityDTO) MappingDTO.convertToDto(country, new CountryWithCityDTO()))
                .collect(Collectors.toList());
    }
}
