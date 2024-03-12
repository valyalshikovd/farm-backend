package com.example.farmbackend.controller;


import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;
    @RolesAllowed({"ADMIN"})
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){

        ProductDto savedProduct = productService.createProduct(productDto);
        return  new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
    @RolesAllowed({"ADMIN"})
    @GetMapping("/{name}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("name") String name){
        return ResponseEntity.ok(productService.getProductByName(name));
    }
    @RolesAllowed({"ADMIN"})
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProductDto(){
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    @RolesAllowed({"ADMIN"})
    @PutMapping("/{name}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("name") String name,
                                                      @RequestBody ProductDto updateProduct){
        ProductDto productDto = productService.updateProduct(name, updateProduct);
        return  ResponseEntity.ok(productDto);
    }
    @RolesAllowed({"ADMIN"})
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable("name")  String name){
        productService.delete(name);
        return  ResponseEntity.ok("Product deleted successfully!");
    }
}
