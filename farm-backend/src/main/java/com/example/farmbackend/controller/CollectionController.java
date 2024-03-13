package com.example.farmbackend.controller;


import com.example.farmbackend.dto.CollectionDto;
import com.example.farmbackend.dto.CollectionsParamDto;
import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.models.Collection;
import com.example.farmbackend.models.Employee;
import com.example.farmbackend.service.CollectionService;
import com.example.farmbackend.service.EmployeeService;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;
import static com.example.farmbackend.controller.ControllerUtils.checkEmployeeRole;

@RestController
@RequestMapping("api/v1/collections")
@AllArgsConstructor
public class CollectionController {

    private CollectionService collectionService;
    private ProductService productService;
    private EmployeeService employeeService;


    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> get(
            @PathVariable("id") Long collectionId,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        CollectionDto collectionDto = collectionService.getCollectionById(collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    @PostMapping("/day")
    public ResponseEntity<List<CollectionDto>> getAllDay(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProductDto productDto = productService.getProductByName(paramDto.getType());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(paramDto.getEmployeeEmail());


        List<CollectionDto> collectionDto = collectionService.getByQueryParamDay(productDto.getId(), employeeDto.getId());

        return ResponseEntity.ok(collectionDto);
    }

    @PostMapping("/month")
    public ResponseEntity<List<CollectionDto>> getAllMonth(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ProductDto productDto = productService.getProductByName(paramDto.getType());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(paramDto.getEmployeeEmail());


        List<CollectionDto> collectionDto = collectionService.getByQueryParamMonth(productDto.getId(), employeeDto.getId());

        return ResponseEntity.ok(collectionDto);
    }

    @PostMapping("/week")
    public ResponseEntity<List<CollectionDto>> getAllWeek(
            @RequestBody CollectionsParamDto paramDto,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProductDto productDto = productService.getProductByName(paramDto.getType());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(paramDto.getEmployeeEmail());


        List<CollectionDto> collectionDto = collectionService.getByQueryParamWeek(productDto.getId(), employeeDto.getId());

        return ResponseEntity.ok(collectionDto);
    }



    @PostMapping("/create")
    public ResponseEntity createCollection(@RequestBody CollectionsParamDto params,
                                           Authentication authentication){
        if(!checkEmployeeRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        System.out.println(authentication.getDetails().toString());

        ProductDto productDto = productService.getProductByName(params.getType());
        EmployeeDto employeeDto = employeeService.getEmployeeByName(params.getEmployeeEmail());

        CollectionDto collectionDto = CollectionDto
                .builder()
                .employeeDto(employeeDto)
                .productDto(productDto)
                .amount(params.getAmount())
                .build();

        collectionService.addCollection(collectionDto);

        return  ResponseEntity.ok().build();
    }

}
