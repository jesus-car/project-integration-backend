package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTO;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPropertyService {

    Page<PropertyDTO> findAll(PropertyFilterDTO filter, Pageable pageable);
}
