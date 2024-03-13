package com.example.farmbackend.controller;


import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto,
                                                    Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProductDto savedProduct = productService.createProduct(productDto);
        return  new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("name") String name,
                                                     Authentication authentication){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProductDto(Authentication authentication){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{name}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("name") String name,
                                                      @RequestBody ProductDto updateProduct,
                                                    Authentication authentication){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProductDto productDto = productService.updateProduct(name, updateProduct);
        return  ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable("name")  String name,
                                                Authentication authentication){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        productService.delete(name);
        return  ResponseEntity.ok("Product deleted successfully!");
    }
}
