package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.CategoryDTOInput;
import com.dh.roomly.dto.impl.CategoryDTOOutput;
import com.dh.roomly.exception.MissingImageException;
import com.dh.roomly.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(value="/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDTOOutput> createCategory(@RequestPart CategoryDTOInput categoryDTOInput,
                                                            @RequestParam("image") MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            throw new MissingImageException("La imagen es obligatoria y no puede estar vacía.");
        }
        CategoryDTOOutput categoryDTOOutput = categoryService.createCategory(categoryDTOInput, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTOOutput);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDTOOutput> updateCategory(@PathVariable Short id,
                                                            @RequestPart CategoryDTOInput categoryDTOInput,
                                                            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        CategoryDTOOutput updatedCategory = categoryService.updateCategory(id, categoryDTOInput, image);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Short id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}