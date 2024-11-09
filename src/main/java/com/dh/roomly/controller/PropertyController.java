package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.PropertyDTOOutput;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.dto.impl.PropertyDTOInput;
import com.dh.roomly.dto.impl.PropertyDetailsDTOOutput;
import com.dh.roomly.exception.MissingImageException;
import com.dh.roomly.service.IPropertyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/properties")
public class PropertyController {
    @Autowired
    private IPropertyService iPropertyService;

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDetailsDTOOutput> getProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(this.iPropertyService.findById(propertyId));
    }

    @DeleteMapping("/{propertyId}")
    public void delete(@PathVariable("propertyId") Long id) {
        this.iPropertyService.delete(id);
    }

    @PostMapping("/filter")
    public Page<PropertyDTOOutput> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                           @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size,
                                           @RequestBody @Valid PropertyFilterDTO filter){
        return this.iPropertyService.findAll(filter, PageRequest.of(page, size));
    }

    @PostMapping(value="/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDTOOutput> createPropertyWithPhotos(@Valid @RequestPart("property") PropertyDTOInput dto,
                                                                      @RequestParam("mainImage") MultipartFile mainImage,
                                                                      @RequestParam("images") List<MultipartFile> images) throws IOException {
        if (mainImage == null || mainImage.isEmpty()) {
            throw new MissingImageException("La imagen principal (mainImage) es obligatoria y no puede estar vacía.");
        }
        if (images == null || images.isEmpty() || images.size() < 4 || images.size() > 5) {
            throw new MissingImageException("Se deben proporcionar entre 4 y 5 imágenes adicionales.");
        }
        if (images.stream().anyMatch(MultipartFile::isEmpty)) {
            throw new MissingImageException("Cada imagen adicional debe ser no vacía.");
        }

        // Insertamos mainImage al inicio de la lista de imágenes
        List<MultipartFile> allImages = new ArrayList<>();
        allImages.add(mainImage);
        allImages.addAll(images);

        PropertyDTOOutput createdProperty = iPropertyService.createPropertyWithPhotos(dto, allImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

    @GetMapping("/admin/list")
    public ResponseEntity<List<PropertyDTOOutput>> findAllForAdmin() {
        List<PropertyDTOOutput> properties = this.iPropertyService.findAllForAdmin();
        return ResponseEntity.ok(properties);
    }

    @PutMapping(value = "/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDTOOutput> updateProperty(@PathVariable Long propertyId,
                                                            @Valid @RequestPart("property") PropertyDTOInput dto,
                                                            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
                                                            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {
        // Validación de imágenes adicionales (si se proporcionan)
        if (images != null && (images.size() < 4 || images.size() > 5)) {
            throw new MissingImageException("Se deben proporcionar entre 4 y 5 imágenes adicionales si se envían.");
        }
        if (images != null && images.stream().anyMatch(MultipartFile::isEmpty)) {
            throw new MissingImageException("Cada imagen adicional debe ser no vacía.");
        }

        PropertyDTOOutput updatedProperty = iPropertyService.updateProperty(propertyId, dto,images,mainImage);
        return ResponseEntity.ok(updatedProperty);
    }
}
