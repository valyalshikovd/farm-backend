package com.example.farmbackend.controller;

import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;

/**
 * Контроллер, отвечающий за управление продуктами.
 *
 * @author Дмитрий Валяльщиков
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;

    /**
     * Создает новый продукт.
     *
     * @param productDto Объект с данными нового продукта.
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 201 (Created) и данными созданного продукта,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора.
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto,
                                                    Authentication authentication
    ) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            ProductDto savedProduct = productService.createProduct(productDto);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Получает продукт по его имени.
     *
     * @param name Имя продукта.
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и данными продукта,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если продукт не найден.
     */
    @GetMapping("/{name}")
    public ResponseEntity<ProductDto> getProductByName(@PathVariable("name") String name,
                                                       Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(productService.getProductByName(name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Получает список всех продуктов.
     *
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и списком всех продуктов,
     *         или статус 403 (Forbidden), если текущий сотрудник не имеет прав администратора,
     *         или статус 404 (Not Found), если продукты не найдены.
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProductDto(Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<ProductDto> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Обновляет информацию о продукте.
     *
     * @param name Имя продукта (используется для поиска).
     * @param updateProduct Объект с обновленными данными продукта.
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и обновленными данными продукта,
     *         или статус 403 (Forbidden), если текущий сотрудник не имеет прав администратора,
     *         или статус 404 (Not Found), если продукт не найден.
     */
    @PutMapping("/{name}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("name") String name,
                                                    @RequestBody ProductDto updateProduct,
                                                    Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            ProductDto productDto = productService.updateProduct(name, updateProduct);
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Удаляет продукт.
     *
     * @param name Имя продукта.
     * @param authentication Данные аутентификации текущего пользователя.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и сообщением об успешном удалении,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если продукт не найден.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable("name") String name,
                                                Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            productService.delete(name);
            return ResponseEntity.ok("Product deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
