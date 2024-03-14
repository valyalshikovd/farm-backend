package com.example.farmbackend.service;

import com.example.farmbackend.dto.ProductDto;

import java.util.List;

/**
 * Интерфейс, предстставляющий методы ProductService.
 */
public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductByName(String name);
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(String name, ProductDto productDto);
    void delete(String name);
}
