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
/**
 * Сервис для управления сборами продукции.
 *
 * @author Дмитрий Валяльщиков
 */
@Service
@AllArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private CollectionRepository collectionRepository;
    private ProductService productService;
    private EmployeeService employeeService;

    /**
     * Добавляет сбор продукции в базу данных.
     *
     * @param collectionDto Объект Dto с данными о сборе.
     * @return True, если сбор добавлен успешно, false - в другом случае.
     * @throws IllegalArgumentException - если продукт или сотрудник с указанными ID не найдены.
     */
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

        try {
            collectionRepository.save(collection);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Получает сбор продукции по его идентификатору.
     *
     * @param id Идентификатор сбора продукции.
     * @return Объект Dto с данными о сборе.
     * @throws NotFoundException - если сбор с данным ID не найден.
     */
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

    /**
     * Методы получающие список сборов продукции по заданным критериям.
     *
     * Для дня, недели и месяца соответственно
     * @param productId Идентификатор продукта (может быть null).
     * @param employeeId Идентификатор сотрудника (может быть null).
     * @return Список объектов Dto с данными о сборах.
     */
    @Override
    public List<CollectionDto> getByQueryParamDay(Long productId, Long employeeId) {
        return generate(productId, employeeId, DateMode.DAY);
    }

    @Override
    public List<CollectionDto> getByQueryParamMonth(Long productId, Long employeeId) {
        return generate(productId, employeeId, DateMode.MONTH);
    }

    @Override
    public List<CollectionDto> getByQueryParamWeek(Long productId, Long employeeId) {
        return generate(productId, employeeId, DateMode.WEEK);
    }

    /**
     * Удаляет сбор продукции по его идентификатору.
     *
     * @param id Идентификатор сбора продукции.
     * @return True, если сбор успешно удален.
     */
    @Override
    public boolean deleteById(Long id) {
        collectionRepository.deleteById(id);
        return true;
    }


    /**
     * Преобразует список  объектов информации о сборе в список DTO объектов.
     *
     * @param collectionList Список сущностей объектов информации о сборе.
     * @return Список DTO объектов информации о сборе.
     */
    private List<CollectionDto> listCollectionMapper(List<Collection> collectionList){
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


    /**
     * Фильтрует список DTO объектов  по дате создания.
     *
     * @param collectionDtoList Исходный список DTO объектов сбора.
     * @param time  максимальное время (в миллисекундах) которое пройтет с момента создания объекта
     * @return Отфильтрованный список DTO объектов ,
     *         созданных за указанный промежуток времени.
     */
    private List<CollectionDto> generateCollectionsWithValideDate(List<CollectionDto> collectionDtoList, long time){
        List<CollectionDto> res = new LinkedList<>();
        for (CollectionDto collectionDto : collectionDtoList){
            if(new Date().getTime() - collectionDto.getDateCreating().getTime() < time){
                res.add(collectionDto);
            }
        }
        return res;
    }

    /**
     * Формирует список DTO объектов `CollectionDto` по заданным критериям
     * и фильтрует их по дате создания.
     *
     * @param productId Идентификатор продукта (может быть null).
     * @param employeeId Идентификатор сотрудника (может быть null).
     * @param dateMode Режим фильтрации по дате (день, месяц, неделя).
     * @return Отфильтрованный список DTO объектов `CollectionDto`
     *         по выбранным критериям и за указанный промежуток времени.
     */
    private List<CollectionDto> generate(Long productId, Long employeeId, int dateMode){
        List<Collection> collectionList;
        if(employeeId == null && productId == null){
            collectionList = collectionRepository.findAll();
            return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  dateMode);
        }
        if (employeeId == null) {
            collectionList = collectionRepository.findAllByProductId(productId);
            return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  dateMode);
        }
        if(productId == null) {
            collectionList = collectionRepository.findAllByEmployeeId(employeeId);
            return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  dateMode);
        }
        collectionList = collectionRepository.findAllByEmployeeIdAndProductId(employeeId, productId);
        return generateCollectionsWithValideDate(listCollectionMapper(collectionList),  dateMode);
    }
}
