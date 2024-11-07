package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.CountryDTO;
import com.dh.roomly.entity.CountryEntity;
import com.dh.roomly.repository.ICountryRepository;
import com.dh.roomly.service.ICountryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements ICountryService {

    private ICountryRepository countryRepository;

    public CountryDTO findById(Short id) {
        CountryEntity country = countryRepository.findByIdWithCities(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
        return (CountryDTO) MappingDTO.convertToDto(country, new CountryDTO());
    }

    @Override
    public List<CountryDTO> findAll() {
        List<CountryEntity> countries = countryRepository.findAllWithCities();
        return countries.stream()
                .map(country -> (CountryDTO) MappingDTO.convertToDto(country, new CountryDTO()))
                .collect(Collectors.toList());
    }
}
