package com.dh.roomly.service;

import com.dh.roomly.dto.impl.CountryWithCityDTO;

import java.util.List;

public interface ICountryService {

    CountryWithCityDTO findById(Short id);
    List<CountryWithCityDTO> findAll();
}
