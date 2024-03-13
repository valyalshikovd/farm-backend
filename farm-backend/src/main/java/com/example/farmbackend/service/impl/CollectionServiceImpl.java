package com.example.farmbackend.service.impl;

import com.example.farmbackend.dto.CollectionDto;
import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.dto.ProductDto;
import com.example.farmbackend.exception.NotFoundException;
import com.example.farmbackend.models.Collection;
import com.example.farmbackend.repository.CollectionRepository;
import com.example.farmbackend.service.CollectionService;
import com.example.farmbackend.service.DateMode;
import com.example.farmbackend.service.EmployeeService;
import com.example.farmbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private CollectionRepository collectionRepository;
    private ProductService productService;
    private EmployeeService employeeService;

    @Override
    public boolean addCollection(CollectionDto collectionDto) {

        ProductDto product  = productService.getProductById(collectionDto.getProductDto().getId());
        EmployeeDto employeeDto = employeeService.getEmployeeById(collectionDto.getEmployeeDto().getId());

        if(product == null || employeeDto == null){
            return false;
        }

        Collection collection = Collection
                .builder()
                .id(collectionDto.getId())
                .productId(collectionDto.getProductDto().getId())
                .employeeId(collectionDto.getEmployeeDto().getId())
                .amount(collectionDto.getAmount())
                .dateCreating(new Date())
                .build();

        System.out.println(collection.toString());


        collectionRepository.save(collection);

        return true;
    }

    @Override
    public CollectionDto getCollectionById(Long id) {

        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Не найдено")
        );

        ProductDto product = productService.getProductById(collection.getProductId());
        EmployeeDto employeeDto = employeeService.getEmployeeById(collection.getEmployeeId());

        CollectionDto collectionDto = CollectionDto
                .builder()
                .id(collection.getId())
                .productDto(product)
                .employeeDto(employeeDto)
                .dateCreating(collection.getDateCreating())
                .amount(collection.getAmount())
                .build();
        return collectionDto;
    }

    @Override
    public List<CollectionDto> getByQueryParamDay(Long productId, Long employeeId) {
        Optional<Collection> collectionList = collectionRepository.findAllByEmployeeIdAndProductId(employeeId, productId);
        return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  DateMode.DAY);
    }

    @Override
    public List<CollectionDto> getByQueryParamMonth(Long productId, Long employeeId) {
        Optional<Collection> collectionList = collectionRepository.findAllByEmployeeIdAndProductId(employeeId, productId);
        return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  DateMode.MONTH);
    }

    @Override
    public List<CollectionDto> getByQueryParamWeek(Long productId, Long employeeId) {
        Optional<Collection> collectionList = collectionRepository.findAllByEmployeeIdAndProductId(employeeId, productId);
        return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  DateMode.WEEK);
    }


    @Override
    public boolean deleteById(Long id) {
        collectionRepository.deleteById(id);
        return true;
    }


    private List<CollectionDto> listCollectionMapper(Optional<Collection> collectionList){
        return collectionList.stream().map( collection -> {
            CollectionDto collectionDto = CollectionDto
                    .builder()
                    .id(collection.getId())
                    .productDto(productService.getProductById(collection.getProductId()))
                    .employeeDto(employeeService.getEmployeeById(collection.getEmployeeId()))
                    .dateCreating(collection.getDateCreating())
                    .amount(collection.getAmount())
                    .build();
            return collectionDto;

        }).toList();
    }

    private List<CollectionDto> generateCollectionsWithValideDate(List<CollectionDto> collectionDtoList, long time){
        List<CollectionDto> res = new LinkedList<>();
        for (CollectionDto collectionDto : collectionDtoList){
            if(new Date().getTime() - collectionDto.getDateCreating().getTime() < time){
                res.add(collectionDto);
            }
        }
        return res;
    }
}
