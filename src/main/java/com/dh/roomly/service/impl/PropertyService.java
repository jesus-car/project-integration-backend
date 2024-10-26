package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.PropertyDTO;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.entity.PropertyEntity;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.repository.specification.PropertySpecification;
import com.dh.roomly.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PropertyService implements IPropertyService {

    @Autowired
    IPropertyRepository iPropertyRepository;
    
    @Override
    public Page<PropertyDTO> findAll(PropertyFilterDTO filter, Pageable pageable) {
        Specification<PropertyEntity> specification = this.addFilters(filter);
        Page<PropertyEntity> property = iPropertyRepository.findAll(specification, pageable);
        return property.map(propertyEntity -> (PropertyDTO) MappingDTO.convertToDto(propertyEntity, new PropertyDTO()));
    }

    @Override
    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        PropertyEntity property = (PropertyEntity) MappingDTO.convertToEntity(propertyDTO, PropertyEntity.class);
        PropertyEntity savedProperty = iPropertyRepository.save(property);
        return (PropertyDTO) MappingDTO.convertToDto(savedProperty, new PropertyDTO());
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
