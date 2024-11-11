package com.dh.roomly.service.impl;

import com.dh.roomly.common.NotFound;
import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.*;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PropertyService implements IPropertyService {

    private final IPropertyRepository iPropertyRepository;
    private final IFileService fileService;
    private final ICategoryRepository categoryRepository;
    private final ICityRepository cityRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PropertyDetailsDTOOutput findById(Long id) {
        PropertyEntity property = iPropertyRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        PropertyDetailsDTOOutput propertyDTO = (PropertyDetailsDTOOutput) MappingDTO.convertToDto(property, new PropertyDetailsDTOOutput());
        // Asignamos mainPhotoUrl como la primera imagen y el resto a photoUrls
        if (!property.getPhotos().isEmpty()) {
            propertyDTO.setMainPhotoUrl(mapUrlToFileEntity(property.getPhotos().get(0))); // Asignar primera imagen
            propertyDTO.setPhotoUrls(mapUrlsToPropertyDTO(property.getPhotos().subList(1,property.getPhotos().size()))); // Asignar el resto
        }
        // Crear manualmente UserSimpleDTOOutput si el owner no es nulo
        if (property.getOwner() != null) {
            UserSimpleDTOOutput ownerDTO = getUserSimpleDTOOutput(property);
            propertyDTO.setOwner(ownerDTO);
        }
        // Asignar manualmente la categoría si no está asignada
        if (property.getCategory() != null) {
            CategoryDTOOutput categoryDTO = new CategoryDTOOutput();
            categoryDTO.setId(property.getCategory().getId());
            categoryDTO.setTitle(property.getCategory().getTitle());
            categoryDTO.setDescription(property.getCategory().getDescription());
            propertyDTO.setCategory(categoryDTO);
        }

        return propertyDTO;
    }

    private UserSimpleDTOOutput getUserSimpleDTOOutput(PropertyEntity property) {
        UserEntity ownerEntity = property.getOwner();
        return new UserSimpleDTOOutput(
                ownerEntity.getId(),
                ownerEntity.getFirstName(),
                ownerEntity.getLastName(),
                ownerEntity.getProfilePhoto()
        );
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
        return property.map(propertyEntity -> {
            try {
                propertyEntity.getCity().getCountry().setCities(null);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            PropertyDTOOutput propertyDTOOutput =
                    (PropertyDTOOutput) MappingDTO.convertToDto(propertyEntity, new PropertyDTOOutput());
            propertyDTOOutput.setCategoryId(propertyEntity.getCategoryId());
            try {
                propertyDTOOutput.setCityId(propertyEntity.getCity().getId());
                propertyDTOOutput.setCountryId(propertyEntity.getCity().getCountry().getId());
                propertyDTOOutput.setPhotoUrls(propertyEntity.getPhotos().stream()
                        .map(FileEntity::getUrl)
                        .collect(Collectors.toList()));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return propertyDTOOutput;
        });
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
        // Verificar la existencia de la categoria y obtener la entidad
        CategoryEntity category = categoryRepository.findById(propertyDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + propertyDTO.getCategoryId()));

        // Convertir el DTO a entidad y asignar la categoría
        PropertyEntity property = (PropertyEntity) MappingDTO.convertToEntity(propertyDTO, PropertyEntity.class);
        property.setCategory(category); //asociamoes la categoria
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
            dtoOutput.setPhotoUrls(mapUrlsToPropertyDTO(photos.subList(1, photos.size()))); // Asignar el resto
        }
        dtoOutput.setOwnerId(owner.getId()); // Asignar ownerId al DTO de salida
        dtoOutput.setCategoryId(category.getId());// Asignar el categoryId al DTO de salida
        dtoOutput.setCountryId(city.getCountry().getId());// Asignar el countryId al DTO de salida

        return dtoOutput;
    }

    private List<String> mapUrlsToPropertyDTO(List<FileEntity> photos){
        return photos.stream()
                .map(FileEntity::getUrl)
                .collect(Collectors.toList());
    }

    private String mapUrlToFileEntity(FileEntity photo) {
        return photo.getUrl();
    }


    private List<FileEntity> uploadPropertyPhotos(List<MultipartFile> files) throws IOException {
        return fileService.uploadFiles(files);
    }

    private FileEntity uploadPropertyPhoto(MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
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
                        propertyDTO.setOwnerId(property.getOwner().getId()); // Asignar ownerId al DTO de salida
                    }
                    // Inicializa la lista de fotos para evitar LazyInitializationException
                    if (property.getPhotos() != null) {
                        // Esto fuerza la carga de la colección de fotos
                        property.getPhotos().size(); // Solo para inicializar la colección

                        // Asigna la primera imagen como mainImageUrl y el resto a photoUrls
                        List<String> photoUrls = mapUrlsToPropertyDTO(property.getPhotos());
                        propertyDTO.setMainPhotoUrl(photoUrls.get(0)); // Asigna la primera foto como main image
                        propertyDTO.setPhotoUrls(photoUrls.subList(1, photoUrls.size())); // Asigna el resto a photoUrls
                    }
                    return propertyDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PropertyDTOOutput updateProperty(Long propertyId, PropertyDTOInput propertyDTO, List<MultipartFile> images, MultipartFile mainImage) throws IOException {
        // Verificar la existencia de la propiedad
        PropertyEntity property = this.findPropertyEntityById(propertyId);

        // Verificar si el nuevo nombre está en uso por otra propiedad
        if (!property.getName().equals(propertyDTO.getName()) && iPropertyRepository.existsByName(propertyDTO.getName())) {
            throw new DuplicateResourceException("El nombre '" + propertyDTO.getName() + "' ya está en uso. Por favor, elige otro nombre.");
        }
        // Verificar la existencia de la ciudad y del propietario
        CityEntity city = cityRepository.findById(propertyDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("La ciudad con ID '" + propertyDTO.getCityId() + "' no existe."));
        UserEntity owner = userRepository.findById(propertyDTO.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID '" + propertyDTO.getOwnerId() + "' no existe."));
        // Verificar la existencia de la categoria y obtener la entidad
        CategoryEntity category = categoryRepository.findById(propertyDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + propertyDTO.getCategoryId()));

        // Actualizar los campos de la propiedad
        property.setName(propertyDTO.getName());
        property.setDescription(propertyDTO.getDescription());
        property.setPricePerNight(propertyDTO.getPricePerNight());
        property.setExactAddress(propertyDTO.getExactAddress());
        property.setMaxCapacity(propertyDTO.getMaxCapacity());
        property.setNumRooms(propertyDTO.getNumRooms());
        property.setNumBeds(propertyDTO.getNumBeds());
        property.setNumBathrooms(propertyDTO.getNumBathrooms());
        property.setCity(city);  // Asociamos la ciudad
        property.setOwner(owner);  // Asociar el propietario

        // Asignar la categoría
        property.setCategory(category); //asociamoes la categoria

        // Manejar las fotos
        handlePropertyImages(property, images, mainImage);

        // Guardar los cambios en la propiedad y en el propietario
        PropertyEntity updatedProperty = iPropertyRepository.save(property);
        owner.getProperties().add(updatedProperty);
        userRepository.save(owner);

        // Convertir a DTO de salida y asignar URLs de fotos
        PropertyDTOOutput dtoOutput = (PropertyDTOOutput) MappingDTO.convertToDto(updatedProperty, new PropertyDTOOutput());
        if (property.getPhotos() != null && !property.getPhotos().isEmpty()) {
            dtoOutput.setMainPhotoUrl(mapUrlToFileEntity(property.getPhotos().get(0))); // Asignar primera imagen
            dtoOutput.setPhotoUrls(mapUrlsToPropertyDTO(property.getPhotos().subList(1, property.getPhotos().size()))); // Asignar el resto
        }

        dtoOutput.setOwnerId(owner.getId()); // Asignar ownerId al DTO de salida
        dtoOutput.setCategoryId(category.getId());// Asignar el categoryId al DTO de salida
        dtoOutput.setCountryId(city.getCountry().getId());// Asignar el countryId al DTO de salida

        return dtoOutput;
    }

    private void handlePropertyImages(PropertyEntity property, List<MultipartFile> images, MultipartFile mainImage) throws IOException {
        // Obtener las fotos actuales
        List<FileEntity> currentPhotos = property.getPhotos();

        if (mainImage != null) {
            // Si se envía una mainImage, reemplazamos la primera imagen
            FileEntity mainPhoto = uploadPropertyPhoto(mainImage); // Cargar la imagen principal
            if (currentPhotos.isEmpty()) {
                // Si no hay fotos, agregamos la nueva como la primera foto
                currentPhotos.add(mainPhoto);
            } else {
                // Si ya hay fotos, reemplazamos la primera
                currentPhotos.set(0, mainPhoto);
            }
        }

        if (images != null && !images.isEmpty()) {
            // Si se envían otras imágenes (no la mainImage), agregamos o reemplazamos las imágenes siguientes
            List<FileEntity> newPhotos = uploadPropertyPhotos(images); // Cargar las nuevas imágenes
            if (currentPhotos.isEmpty()) {
                // Si no hay fotos, agregamos todas las nuevas imágenes
                currentPhotos.addAll(newPhotos);
            } else {
                // Si ya hay fotos, reemplazamos las imágenes después de la primera
                if (currentPhotos.size() > 1) {
                    currentPhotos.subList(1, currentPhotos.size()).clear();
                }
                currentPhotos.addAll(newPhotos);
            }
        }

        // Si no se envía ninguna imagen (mainImage o images), las fotos permanecen igual
        property.setPhotos(currentPhotos);
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
        if (Objects.nonNull(filter.getCategoryId())) {
            spec = spec.and(PropertySpecification.shortEqualTo(filter.getCategoryId(), "categoryId"));
        }
        if (Objects.nonNull(filter.getCityId())) {
            spec = spec.and(PropertySpecification.cityEqualTo(filter.getCityId()));
        }
        if (Objects.nonNull(filter.getCountryId())) {
            spec = spec.and(PropertySpecification.countryEqualTo(filter.getCountryId()));
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
