package com.dh.roomly.service;

import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.dto.impl.PropertyDTOInput;
import com.dh.roomly.dto.impl.PropertyDetailsDTOOutput;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPropertyService {


    PropertyDetailsDTOOutput findById(Long id);

    void delete(Long id);

    Page<PropertyDTOOutput> findAll(PropertyFilterDTO filter, Pageable pageable);

    @Transactional
    PropertyDTOOutput createPropertyWithPhotos(PropertyDTOInput propertyDTO, MultipartFile mainImage, List<MultipartFile> files) throws IOException;

    List<PropertyDTOOutput> findAllForAdmin();

    PropertyDTOOutput updateProperty(Long propertyId, @Valid PropertyDTOInput dto, List<MultipartFile> images, MultipartFile mainImage, String mainImageUrl, List<String> imageUrls) throws IOException;
}
