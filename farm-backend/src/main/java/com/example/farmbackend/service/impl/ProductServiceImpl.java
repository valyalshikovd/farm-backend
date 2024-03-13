package com.example.farmbackend.service.impl;

import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.exception.NotFoundException;
import com.example.farmbackend.mapper.EmployeeMapper;
import com.example.farmbackend.mapper.ProductMapper;
import com.example.farmbackend.models.Employee;
import com.example.farmbackend.models.Product;
import com.example.farmbackend.repository.ProductRepository;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToModel(productDto);
        Product savedProduct = productRepository.save(product);
        return  ProductMapper.mapToDto(savedProduct);
    }

    @Override
    public ProductDto getProductByName(String name) {
        Product product = productRepository.findByName(name);
        return ProductMapper.mapToDto(product);
    }

    @Override
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee is not exists with given id : " + id)
        );
        return ProductMapper.mapToDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(String name, ProductDto productDto) {
        Product product = productRepository.findByName(name);


        product.setName(productDto.getName());
        product.setMeasure(productDto.getName());

        productRepository.save(product);
        return ProductMapper.mapToDto(product);
    }

    @Override
    public void delete(String name) {
        Product product = productRepository
                .findByName(name);
        ///
        productRepository.deleteById(product.getId());
    }
}
