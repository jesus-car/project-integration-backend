package com.dh.roomly.service.impl;

import com.dh.roomly.common.NotFound;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.dto.impl.PropertyDTOInput;
import com.dh.roomly.entity.CategoryEntity;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.entity.PropertyEntity;
import com.dh.roomly.exception.DuplicateResourceException;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICategoryRepository;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.repository.specification.PropertySpecification;
import com.dh.roomly.service.IFileService;
import com.dh.roomly.service.IPropertyService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyService implements IPropertyService {

    private final IPropertyRepository iPropertyRepository;
    private final IFileService fileService;
    private final ICategoryRepository categoryRepository;

    @Override
    public PropertyDTOOutput findById(Long id) {
        return (PropertyDTOOutput) MappingDTO.convertToDto(
                this.findPropertyEntityById(id), new PropertyDTOOutput());
    }

    @Override
    public void delete(Long id) {
        this.findById(id);
        this.iPropertyRepository.deleteById(String.valueOf(id));
    }

    @Override
    public Page<PropertyDTOOutput> findAll(PropertyFilterDTO filter, Pageable pageable) {
        Specification<PropertyEntity> specification = this.addFilters(filter);
        Page<PropertyEntity> property = iPropertyRepository.findAll(specification, pageable);
        return property.map(propertyEntity -> (PropertyDTOOutput) MappingDTO.convertToDto(propertyEntity, new PropertyDTOOutput()));
    }

    @Override
    @Transactional
    public PropertyDTOOutput createPropertyWithPhotos(PropertyDTOInput propertyDTO, List<MultipartFile> files) throws IOException {
        if (iPropertyRepository.existsByName(propertyDTO.getName())) {
            throw new DuplicateResourceException("El nombre '" + propertyDTO.getName() + "' ya está en uso. Por favor, elige otro nombre.");
        }
        PropertyEntity property = (PropertyEntity) MappingDTO.convertToEntity(propertyDTO, PropertyEntity.class);
        assignCategoryToProperty(propertyDTO.getCategoryId(), property);
        List<FileEntity> photos = uploadPropertyPhotos(files);
        property.setPhotos(photos);
        PropertyEntity savedProperty = iPropertyRepository.save(property);

        PropertyDTOOutput dtoOutput = (PropertyDTOOutput) MappingDTO.convertToDto(savedProperty, new PropertyDTOOutput());

        List<String> photoUrls = photos.stream()
                .map(FileEntity::getUrl)
                .collect(Collectors.toList());
        dtoOutput.setPhotoUrls(photoUrls);

        return dtoOutput;
    }

    private void assignCategoryToProperty(Short categoryId, PropertyEntity property) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoryId));
        property.setCategory(category);
    }

    private List<FileEntity> uploadPropertyPhotos(List<MultipartFile> files) throws IOException {
        return fileService.uploadFiles(files);
    }

    @Override
    @Transactional
    public List<PropertyDTOOutput> findAllForAdmin() {
        List<PropertyEntity> properties = iPropertyRepository.findAll();

        return properties.stream()
                .map(property -> {
                    PropertyDTOOutput propertyDTO = (PropertyDTOOutput) MappingDTO.convertToDto(property, new PropertyDTOOutput());

                    // Inicializa la lista de fotos para evitar LazyInitializationException
                    if (property.getPhotos() != null) {
                        // Esto fuerza la carga de la colección de fotos
                        property.getPhotos().size(); // Solo para inicializar la colección
                    }

                    // Lógica para mapear las fotos a URLs
                    List<String> photoUrls = property.getPhotos().stream()
                            .map(FileEntity::getUrl)
                            .collect(Collectors.toList());
                    propertyDTO.setPhotoUrls(photoUrls);

                    return propertyDTO;
                })
                .collect(Collectors.toList());
    }



    private PropertyEntity findPropertyEntityById(Long id){
        return this.iPropertyRepository.findById(String.valueOf(id)).orElseThrow(() -> new ResourceNotFoundException(
                NotFound.NOT_FOUND_PRODUCT.toString()));
    }

    private Specification<PropertyEntity> addFilters(PropertyFilterDTO filter){
        Specification<PropertyEntity> spec = Specification.where(null);
        if (Objects.nonNull(filter.getName())) {
            spec = spec.and(PropertySpecification.hasName(filter.getName()));
        }
        if (Objects.nonNull(filter.getDescription())) {
            spec = spec.and(PropertySpecification.hasDescription(filter.getDescription()));
        }
        if (Objects.nonNull(filter.getExactAddress())) {
            spec = spec.and(PropertySpecification.hasExactAddress(filter.getExactAddress()));
        }
        if (Objects.nonNull(filter.getMaxCapacity())) {
            spec = spec.and(PropertySpecification.shortLessThanOrEqualTo(filter.getMaxCapacity(), "maxCapacity"));
        }
        spec = addPriceFilters(filter, spec);
        spec = addNumBedsFilters(filter, spec);
        spec = addNumBathroomsFilters(filter, spec);
        spec = addNumRoomsFilters(filter, spec);
        return spec;
    }

    private Specification<PropertyEntity> addPriceFilters(PropertyFilterDTO filter, Specification<PropertyEntity> spec){
        if (Objects.nonNull(filter.getMinPricePerNight())) {
            spec = spec.and(PropertySpecification.priceGreaterThanOrEqualTo(filter.getMinPricePerNight()));
        }
        if (Objects.nonNull(filter.getMaxPricePerNight())) {
            spec = spec.and(PropertySpecification.priceLessThanOrEqualTo(filter.getMaxPricePerNight()));
        }
        if (Objects.nonNull(filter.getPricePerNight())) {
            spec = spec.and(PropertySpecification.priceEqualTo(filter.getPricePerNight()));
        }
        return spec;
    }

    private Specification<PropertyEntity> addNumRoomsFilters(PropertyFilterDTO filter, Specification<PropertyEntity> spec){
        final String numRoomsName = "numRooms";
        if (Objects.nonNull(filter.getMinNumRooms())) {
            spec = spec.and(PropertySpecification.shortGreaterThanOrEqualTo(filter.getMinNumRooms(), numRoomsName));
        }
        if (Objects.nonNull(filter.getMaxNumRooms())) {
            spec = spec.and(PropertySpecification.shortLessThanOrEqualTo(filter.getMaxNumRooms(), numRoomsName));
        }
        if (Objects.nonNull(filter.getNumRooms())) {
            spec = spec.and(PropertySpecification.shortEqualTo(filter.getNumRooms(), numRoomsName));
        }
        return spec;
    }

    private Specification<PropertyEntity> addNumBedsFilters(PropertyFilterDTO filter, Specification<PropertyEntity> spec){
        final String numBedsName = "numBeds";
        if (Objects.nonNull(filter.getMinNumBeds())) {
            spec = spec.and(PropertySpecification.shortGreaterThanOrEqualTo(filter.getMinNumBeds(), numBedsName));
        }
        if (Objects.nonNull(filter.getMaxNumBeds())) {
            spec = spec.and(PropertySpecification.shortLessThanOrEqualTo(filter.getMaxNumBeds(), numBedsName));
        }
        if (Objects.nonNull(filter.getNumBeds())) {
            spec = spec.and(PropertySpecification.shortEqualTo(filter.getNumBeds(), numBedsName));
        }
        return spec;
    }

    private Specification<PropertyEntity> addNumBathroomsFilters(PropertyFilterDTO filter, Specification<PropertyEntity> spec){
        final String numBathroomsName = "numBathrooms";
        if (Objects.nonNull(filter.getMinNumBathrooms())) {
            spec = spec.and(PropertySpecification.shortGreaterThanOrEqualTo(filter.getMinNumBathrooms(), numBathroomsName));
        }
        if (Objects.nonNull(filter.getMaxNumBathrooms())) {
            spec = spec.and(PropertySpecification.shortLessThanOrEqualTo(filter.getMaxNumBathrooms(), numBathroomsName));
        }
        if (Objects.nonNull(filter.getNumBathrooms())) {
            spec = spec.and(PropertySpecification.shortEqualTo(filter.getNumBathrooms(), numBathroomsName));
        }
        return spec;
    }

}
