package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.CategoryDTOInput;
import com.dh.roomly.dto.impl.CategoryDTOOutput;
import com.dh.roomly.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("v1/categories")
public class CategoryController {

    private ICategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTOOutput> getCategoryById(@PathVariable Short id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTOOutput>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PostMapping("/new")
    public ResponseEntity<CategoryDTOOutput> createCategory(@RequestBody CategoryDTOInput categoryDTOInput) {
        CategoryDTOOutput categoryDTOOutput = categoryService.createCategory(categoryDTOInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTOOutput);
    }
}