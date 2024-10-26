package com.dh.roomly.service;

import com.dh.roomly.dto.impl.ProductDTO;
import com.dh.roomly.dto.filter.ProductFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {

    Page<ProductDTO> findAll(ProductFilterDTO filter, Pageable pageable);
}
