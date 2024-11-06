package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.dto.impl.PropertyDTOInput;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPropertyService {


    PropertyDTOOutput findById(Long id);

    void delete(Long id);

    Page<PropertyDTOOutput> findAll(PropertyFilterDTO filter, Pageable pageable);

    @Transactional
    PropertyDTOOutput createPropertyWithPhotos(PropertyDTOInput propertyDTO, List<MultipartFile> files) throws IOException;

    List<PropertyDTOOutput> findAllForAdmin();
}
