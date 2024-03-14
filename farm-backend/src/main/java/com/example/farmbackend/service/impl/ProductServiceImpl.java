package com.example.farmbackend.service.impl;

import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.exception.NotFoundException;
import com.example.farmbackend.mapper.ProductMapper;
import com.example.farmbackend.models.Product;
import com.example.farmbackend.repository.ProductRepository;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Сервис для управления данными продуктов.
 *
 * @author Дмитрий Валяльщиков
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    /**
     * Создает новый продукт в базе данных.
     *
     * @param productDto DTO объект с данными о продукте.
     * @return DTO объект созданного продукта.
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductMapper.mapToModel(productDto);
        Product savedProduct = productRepository.save(product);
        return  ProductMapper.mapToDto(savedProduct);
    }

    /**
     * Получает продукт по его имени.
     *
     * @param name Имя продукта.
     * @return DTO объект найденного продукта, или null, если продукт не найден.
     */
    @Override
    public ProductDto getProductByName(String name) {
        Product product = productRepository.findByName(name);
        return ProductMapper.mapToDto(product);
    }

    /**
     * Получает продукт по его идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return DTO объект найденного продукта.
     * @throws NotFoundException - если продукт не найден.
     */
    @Override
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee is not exists with given id : " + id)
        );
        return ProductMapper.mapToDto(product);
    }

    /**
     * Получает список всех продуктов.
     *
     * @return Список DTO объектов всех продуктов.
     */
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToDto).collect(Collectors.toList());
    }

    /**
     * Обновляет данные продукта.
     *
     * @param name Имя продукта для обновления (используется для поиска).
     * @param productDto DTO объект с обновленными данными продукта.
     * @return DTO объект обновленного продукта.
     */
    @Override
    public ProductDto updateProduct(String name, ProductDto productDto) {
        Product product = productRepository.findByName(name);


        product.setName(productDto.getName());
        product.setMeasure(productDto.getName());

        productRepository.save(product);
        return ProductMapper.mapToDto(product);
    }

    /**
     * Удаляет продукт по его имени.
     *
     * @param name Имя продукта для удаления.
     * @throws NotFoundException - если продукт не найден.
     */
    @Override
    public void delete(String name) {
        Product product = productRepository
                .findByName(name);
        productRepository.deleteById(product.getId());
    }
}
