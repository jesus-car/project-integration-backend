package com.dh.roomly.service;

import com.dh.roomly.dto.impl.CategoryDTOInput;
import com.dh.roomly.dto.impl.CategoryDTOOutput;

import java.net.URI;
import java.util.List;

public interface ICategoryService {

    CategoryDTOOutput findCategoryById(Short id);
    List<CategoryDTOOutput> findAllCategories();

    CategoryDTOOutput createCategory(CategoryDTOInput categoryDTOInput);
}
