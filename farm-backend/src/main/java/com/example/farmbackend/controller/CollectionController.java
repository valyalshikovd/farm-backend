package com.example.farmbackend.controller;

import com.example.farmbackend.dto.*;
import com.example.farmbackend.service.CollectionService;
import com.example.farmbackend.service.EmployeeService;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;
import static com.example.farmbackend.controller.ControllerUtils.checkEmployeeRole;

/**
 * Контроллер, отвечающий за данные о сборе продукта.
 *
 * @author Дмитрий Валяльщиков
 */
@RestController
@RequestMapping("api/v1/collections")
@AllArgsConstructor
public class CollectionController {
    /**
     * Сервисы для работы с со сборами продуктов, продуктами и сотрудниками.
     */

    private CollectionService collectionService;
    private ProductService productService;
    private EmployeeService employeeService;

    /**
     * Получает сбор продукта по ID.
     * @param collectionId Идентификатор сбора продуктов.
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и данными сбора продуктов,
     *         или статус 403 (Forbidden), если текущий сотрудник не имеет прав администратора.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> get(
            @PathVariable("id") Long collectionId,
            Authentication authentication
    ) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CollectionDto collectionDto = collectionService.getCollectionById(collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    /**
     * Получает список сборов продукта за день.
     *
     * @param paramDto Параметры запроса (тип продукта, email сотрудника, флаг для суммирования данных).
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и списком сборов за день,
     *         или статус 403 (Forbidden), если текущий сотрудник не имеет прав администратора.
     */
    @PostMapping("/day")
    public ResponseEntity<List<CollectionDto>> getAllDay(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CollectionDto> collectionDto = collectionService.getByQueryParamDay(
                getProductId(paramDto.getType()),
                getEmployeeId(paramDto.getEmployeeEmail())
        );

        if (paramDto.isTotal()) {
            collectionDto = createTotalDtoGenerator(collectionDto);
        }

        return ResponseEntity.ok(collectionDto);
    }

    /**
     * Получает список коллекций за месяц.
     *
     * (Описание аналогично методу getAllDay)
     */
    @PostMapping("/month")
    public ResponseEntity<List<CollectionDto>> getAllMonth(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CollectionDto> collectionDto = collectionService.getByQueryParamMonth(
                getProductId(paramDto.getType()),
                getEmployeeId(paramDto.getEmployeeEmail())
        );

        if (paramDto.isTotal()) {
            collectionDto = createTotalDtoGenerator(collectionDto);
        }

        return ResponseEntity.ok(collectionDto);
    }

    /**
     * Получает список коллекций за неделю.
     *
     * (Описание аналогично методу getAllDay)
     */
    @PostMapping("/week")
    public ResponseEntity<List<CollectionDto>> getAllWeek(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CollectionDto> collectionDto = collectionService.getByQueryParamWeek(
                getProductId(paramDto.getType()),
                getEmployeeId(paramDto.getEmployeeEmail())
        );

        if (paramDto.isTotal()) {
            collectionDto = createTotalDtoGenerator(collectionDto);
        }

        return ResponseEntity.ok(collectionDto);
    }


    /**
     * Создает новый объект содержащий информацию о сборе.
     *
     * @param params Параметры нового сбора (тип продукта, email сотрудника, количество).
     * @param authentication Данные аутентификации текущего сотрудника.
     * @return Ответ HTTP-запроса со статусом 200 (OK) при успешном создании,
     *         400 (Bad Request) при ошибке,
     *         403 (Forbidden), если текущий пользователь не имеет прав сотрудника.
     */
    @PostMapping("/create")
    public ResponseEntity createCollection(@RequestBody RequestCollection params,
                                           Authentication authentication) {
        if (!checkEmployeeRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProductDto productDto = productService.getProductByName(params.getType());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(params.getEmployeeEmail());

        CollectionDto collectionDto = CollectionDto
                .builder()
                .employeeDto(employeeDto)
                .productDto(productDto)
                .amount(params.getAmount())
                .build();

        try{
            collectionService.addCollection(collectionDto);
        }catch (Exception e){
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Формирует список объектов сборов товаров с суммированными данными.
     *
     * @param collectionDto Список исходных коллекций.
     * @return Список коллекций с суммарным количеством по каждому типу продукта.
     */
    private List<CollectionDto> createTotalDtoGenerator(List<CollectionDto> collectionDto) {
        Map<Long, CollectionDto> map = new HashMap<>();

        for (CollectionDto dto : collectionDto) {

            if (map.containsKey(dto.getProductDto().getId())) {
                CollectionDto currDto = map.get(dto.getProductDto().getId());

                currDto.setAmount(currDto.getAmount() + dto.getAmount());

                continue;
            }
            dto.setEmployeeDto(null);
            dto.setId(null);
            dto.setDateCreating(null);
            map.put(dto.getProductDto().getId(), dto);
        }

        return map.values().stream().toList();

    }

    /**
     * Получает ID продукта по его типу.
     *
     * @param type Тип продукта.
     * @return ID продукта, null - если продукт не найден.
     */
    private Long getProductId(String type) {

        ProductDto productDto = productService.getProductByName(type);
        Long productId;
        if (productDto == null) {
            productId = null;
        } else {
            productId = productDto.getId();
        }

        return productId;
    }

    /**
     * Получает ID сотрудника по его email.
     *
     * @param email Email сотрудника.
     * @return ID сотрудника, null - если сотрудник не найден.
     */
    private Long getEmployeeId(String email) {
        EmployeeDto employeeDto = employeeService.getEmployeeByName(email);
        Long employeeId;
        if (employeeDto == null) {
            employeeId = null;
        } else {
            employeeId = employeeDto.getId();
        }

        return employeeId;
    }
}
