package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTO;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPropertyService {

    Page<PropertyDTO> findAll(PropertyFilterDTO filter, Pageable pageable);

    @Transactional
    PropertyDTO createPropertyWithPhotos(PropertyDTO propertyDTO, List<MultipartFile> files) throws IOException;

    List<PropertyDTO> findAllForAdmin();
}
