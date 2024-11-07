package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.CountryWithCityDTO;
import com.dh.roomly.service.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/countries")
public class CountryController {

    @Autowired
    private ICountryService countryService;

    @GetMapping("/{id}")
    public CountryWithCityDTO getCountryById(@PathVariable Short id) {
        return countryService.findById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryWithCityDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }

}