package com.dh.roomly.service.impl;

import com.dh.roomly.dto.common.MappingDTO;
import com.dh.roomly.dto.impl.CategoryDTOOutput;
import com.dh.roomly.entity.CategoryEntity;
import com.dh.roomly.exception.ResourceNotFoundException;
import com.dh.roomly.repository.ICategoryRepository;
import com.dh.roomly.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    ICategoryRepository categoryRepository;

    @Override
    public CategoryDTOOutput findCategoryById(Short id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
        return (CategoryDTOOutput) MappingDTO.convertToDto(category, new CategoryDTOOutput());
    }

    @Override
    public List<CategoryDTOOutput> findAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> (CategoryDTOOutput)MappingDTO.convertToDto(category, new CategoryDTOOutput()))
                .collect(Collectors.toList());
    }
}
