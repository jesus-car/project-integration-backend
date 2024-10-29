package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.PropertyDTO;
import com.dh.roomly.dto.filter.PropertyFilterDTO;
import com.dh.roomly.exception.MissingImageException;
import com.dh.roomly.service.IPropertyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("v1/properties")
public class PropertyController {
    @Autowired
    private IPropertyService iPropertyService;

    @PostMapping("/filter")
    public Page<PropertyDTO> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                    @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size,
                                    @RequestBody @Valid PropertyFilterDTO filter){
        return this.iPropertyService.findAll(filter, PageRequest.of(page, size));
    }

    @PostMapping("/new")
    public ResponseEntity<PropertyDTO> createPropertyWithPhotos(@Valid @RequestPart("propertyDTO") PropertyDTO dto,
                                                                @RequestParam("files") List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty() || images.stream().allMatch(MultipartFile::isEmpty)) {
            throw new MissingImageException("At least one non-empty image must be provided.");
        }
        PropertyDTO createdProperty = iPropertyService.createPropertyWithPhotos(dto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

}
