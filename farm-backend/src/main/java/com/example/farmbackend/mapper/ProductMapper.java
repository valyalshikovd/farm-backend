package com.example.farmbackend.mapper;

import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.models.Product;

public class ProductMapper {
    /**
     * Класс для преобразования объекта-модели в Data-transfer-Object
     */
    public static ProductDto mapToDto(Product product){
        if(product == null){
            return null;
        }
        return  new ProductDto(
                product.getId(),
                product.getName(),
                product.getMeasure()
        );
    }

    public static Product mapToModel(ProductDto productDto){
        if(productDto == null){
            return null;
        }
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getMeasure()
        );
    }
}
