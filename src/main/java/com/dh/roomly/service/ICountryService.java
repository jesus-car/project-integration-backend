package com.dh.roomly.service;

import com.dh.roomly.dto.impl.CountryDTO;

import java.util.List;

public interface ICountryService {

    CountryDTO findById(Short id);
    List<CountryDTO> findAll();
}
