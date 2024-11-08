package com.dh.roomly.service.impl;

import com.dh.roomly.common.NotFound;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.dto.impl.PropertyDTOInput;
import com.dh.roomly.entity.*;
import com.dh.roomly.exception.DuplicateResourceException;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICategoryRepository;
import com.dh.roomly.repository.ICityRepository;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.repository.UserRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyService implements IPropertyService {

    private final IPropertyRepository iPropertyRepository;
    private final IFileService fileService;
    private final ICategoryRepository categoryRepository;
    private final ICityRepository cityRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PropertyDTOOutput findById(Long id) {
        PropertyEntity property = this.findPropertyEntityById(id);
        PropertyDTOOutput propertyDTO = (PropertyDTOOutput) MappingDTO.convertToDto(property, new PropertyDTOOutput());

        // Asigna el nombre del propietario
        if (property.getOwner() != null) {
            propertyDTO.setOwnerName(property.getOwner().getUsername());
        }
        // Asignamos mainPhotoUrl como la primera imagen y el resto a photoUrls
        if (!property.getPhotos().isEmpty()) {
            propertyDTO.setMainPhotoUrl(mapUrlToFileEntity(property.getPhotos().get(0))); // Asignar primera imagen
            propertyDTO.setPhotoUrls(mapUrlsToPropertyDTO(property.getPhotos().subList(1,property.getPhotos().size()), propertyDTO)); // Asignar el resto
        }

        return propertyDTO;
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
        // Verificar la existencia de la ciudad
        CityEntity city = cityRepository.findById(propertyDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("La ciudad con ID '" + propertyDTO.getCityId() + "' no existe."));

        // Verificar la existencia del propietario (usuario) y obtener la entidad del usuario
        UserEntity owner = userRepository.findById(propertyDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID '" + propertyDTO.getOwnerId() + "' no existe."));

        // Convertir el DTO a entidad y asignar la categoría
        PropertyEntity property = (PropertyEntity) MappingDTO.convertToEntity(propertyDTO, PropertyEntity.class);
        assignCategoryToProperty(propertyDTO.getCategoryId(), property);
        property.setCity(city);  // Asociamos la ciudad
        property.setOwner(owner);  // Asociar el propietario

        // Subir y asociar fotos
        List<FileEntity> photos = uploadPropertyPhotos(files);
        property.setPhotos(photos);

        // Guardar la entidad y convertir a DTO de salida
        PropertyEntity savedProperty = iPropertyRepository.save(property);

        // Asociar la propiedad con el usuario y guardar el usuario
        owner.getProperties().add(savedProperty);
        userRepository.save(owner);

        PropertyDTOOutput dtoOutput = (PropertyDTOOutput) MappingDTO.convertToDto(savedProperty, new PropertyDTOOutput());
        // Asignamos mainPhotoUrl como la primera imagen y el resto a photoUrls
        if (!photos.isEmpty()) {
            dtoOutput.setMainPhotoUrl(mapUrlToFileEntity(photos.get(0))); // Asignar primera imagen
            dtoOutput.setPhotoUrls(mapUrlsToPropertyDTO(photos.subList(1, photos.size()), dtoOutput)); // Asignar el resto
        }
        dtoOutput.setOwnerName(owner.getUsername()); // Asignar el nombre del propietario al DTO de salida


        return dtoOutput;
    }

    private List<String> mapUrlsToPropertyDTO(List<FileEntity> photos, PropertyDTOOutput dtoOutput){
        return photos.stream()
                .map(FileEntity::getUrl)
                .collect(Collectors.toList());
    }

    private String mapUrlToFileEntity(FileEntity photo) {
        return photo.getUrl();
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

                    // Fuerza la carga de owner para evitar LazyInitializationException
                    if (property.getOwner() != null) {
                        propertyDTO.setOwnerName(property.getOwner().getUsername());
                    }
                    // Inicializa la lista de fotos para evitar LazyInitializationException
                    if (property.getPhotos() != null) {
                        // Esto fuerza la carga de la colección de fotos
                        property.getPhotos().size(); // Solo para inicializar la colección

                        // Asigna la primera imagen como mainImageUrl y el resto a photoUrls
                        List<String> photoUrls = mapUrlsToPropertyDTO(property.getPhotos(), propertyDTO);
                        propertyDTO.setMainPhotoUrl(photoUrls.get(0)); // Asigna la primera foto como main image
                        propertyDTO.setPhotoUrls(photoUrls.subList(1, photoUrls.size())); // Asigna el resto a photoUrls
                    }
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
