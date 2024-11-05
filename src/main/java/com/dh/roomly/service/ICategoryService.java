package com.dh.roomly.service;

import com.dh.roomly.dto.impl.CategoryDTOOutput;

import java.util.List;

public interface ICategoryService {

    CategoryDTOOutput findCategoryById(Short id);
    List<CategoryDTOOutput> findAllCategories();
}
