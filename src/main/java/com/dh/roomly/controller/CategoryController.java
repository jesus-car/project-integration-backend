package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.CategoryDTOOutput;
import com.dh.roomly.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTOOutput> getCategoryById(@PathVariable Short id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTOOutput>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }
}