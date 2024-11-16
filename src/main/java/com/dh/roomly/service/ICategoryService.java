package com.dh.roomly.service;

import com.dh.roomly.dto.impl.CategoryDTOInput;
import com.dh.roomly.dto.impl.CategoryDTOOutput;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICategoryService {

    CategoryDTOOutput findCategoryById(Short id);
    List<CategoryDTOOutput> findAllCategories();

    CategoryDTOOutput createCategory(CategoryDTOInput categoryDTOInput, MultipartFile image) throws IOException;

    CategoryDTOOutput updateCategory(Short id, CategoryDTOInput categoryDTOInput, MultipartFile image) throws IOException;

    void deleteCategory(Short id);
}
