package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.CategoryDTOInput;
import com.dh.roomly.dto.impl.CategoryDTOOutput;
import com.dh.roomly.entity.CategoryEntity;
import com.dh.roomly.entity.FileEntity;
import com.dh.roomly.exception.DuplicateResourceException;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICategoryRepository;
import com.dh.roomly.repository.IPropertyRepository;
import com.dh.roomly.service.ICategoryService;
import com.dh.roomly.service.IFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final IFileService fileService;
    private  final IPropertyRepository propertyRepository;

    @Override
    public CategoryDTOOutput findCategoryById(Short id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
        CategoryDTOOutput categoryDTOOutput = (CategoryDTOOutput) MappingDTO.convertToDto(category, new CategoryDTOOutput());
        return setUrlToDTO(category, categoryDTOOutput);
    }

    @Override
    public List<CategoryDTOOutput> findAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    CategoryDTOOutput categoryDTOOutput = (CategoryDTOOutput) MappingDTO.convertToDto(category, new CategoryDTOOutput());
                    return setUrlToDTO(category, categoryDTOOutput);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTOOutput createCategory(CategoryDTOInput categoryDTOInput, MultipartFile image) throws IOException {
        if(categoryRepository.existsByTitle(categoryDTOInput.getTitle())){
            throw new DuplicateResourceException("Category with title: " + categoryDTOInput.getTitle() + " already exists");
        }
        CategoryEntity categoryEntity = (CategoryEntity) MappingDTO.convertToEntity(categoryDTOInput, CategoryEntity.class);
        FileEntity fileEntity = fileService.uploadFile(image);
        categoryEntity.setFile(fileEntity);
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);

        CategoryDTOOutput categoryDTOOutput = (CategoryDTOOutput) MappingDTO.convertToDto(savedCategory, new CategoryDTOOutput());
        categoryDTOOutput.setImageUrl(savedCategory.getFile().getUrl());
        return categoryDTOOutput;
    }

    private CategoryDTOOutput setUrlToDTO(CategoryEntity category, CategoryDTOOutput categoryDTOOutput) {
        // Verificar que tanto getFile() como getUrl() no sean null
        if (category.getFile() != null && category.getFile().getUrl() != null) {
            categoryDTOOutput.setImageUrl(category.getFile().getUrl());
        } else {
            // Si alguno es null, asignar un valor predeterminado o dejarlo sin valor
            categoryDTOOutput.setImageUrl(null);
        }
        return categoryDTOOutput;
    }

    @Override
    public CategoryDTOOutput updateCategory(Short id, CategoryDTOInput categoryDTOInput, MultipartFile image) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));

        if (categoryRepository.existsByTitleAndIdNot(categoryDTOInput.getTitle(), id)) {
            throw new DuplicateResourceException("Category with title: " + categoryDTOInput.getTitle() + " already exists");
        }

        // Actualizar los campos básicos del DTO
        categoryEntity.setTitle(categoryDTOInput.getTitle());
        categoryEntity.setDescription(categoryDTOInput.getDescription());

        // Si se proporciona una nueva imagen, actualizarla
        if (image != null && !image.isEmpty()) {
            FileEntity fileEntity = fileService.uploadFile(image);
            categoryEntity.setFile(fileEntity);
        }

        CategoryEntity updatedCategory = categoryRepository.save(categoryEntity);
        CategoryDTOOutput categoryDTOOutput = (CategoryDTOOutput) MappingDTO.convertToDto(updatedCategory, new CategoryDTOOutput());
        return setUrlToDTO(updatedCategory, categoryDTOOutput);
    }

    @Override
    public void deleteCategory(Short id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));

        boolean isCategoryInUse = propertyRepository.existsByCategoryId(id);
        if (isCategoryInUse) {
            throw new IllegalStateException("No se puede eliminar la categoría porque está asociada a una o más propiedades.");
        }
        categoryRepository.delete(categoryEntity);
    }
}
