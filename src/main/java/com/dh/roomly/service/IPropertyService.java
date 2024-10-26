package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTO;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPropertyService {

    Page<PropertyDTO> findAll(PropertyFilterDTO filter, Pageable pageable);

    PropertyDTO createProperty(@Valid PropertyDTO dto);
}
