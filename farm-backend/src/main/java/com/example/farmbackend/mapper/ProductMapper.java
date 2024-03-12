package com.example.farmbackend.mapper;

import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.models.Product;

public class ProductMapper {


    public static ProductDto mapToDto(Product product){
        return  new ProductDto(
                product.getId(),
                product.getName(),
                product.getMeasure()
        );
    }


    public static Product mapToModel(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getMeasure()
        );
    }
}
