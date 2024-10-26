package com.dh.roomly.controller;

import com.dh.roomly.dto.impl.ProductDTO;
import com.dh.roomly.dto.filter.ProductFilterDTO;
import com.dh.roomly.service.IProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @PostMapping("/filter")
    public Page<ProductDTO> findAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                    @RequestParam(defaultValue = "10") @Min(0) @Max(100) int size,
                                    @RequestBody @Valid ProductFilterDTO filter){
        return this.iProductService.findAll(filter, PageRequest.of(page, size));
    }

}
