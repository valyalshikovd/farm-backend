package com.example.farmbackend.service;

import com.example.farmbackend.dto.CollectionDto;

import java.util.List;

public interface CollectionService {
    boolean addCollection(CollectionDto collectionDto);
    CollectionDto getCollectionById(Long id);
    List<CollectionDto> getByQueryParamDay(Long productId, Long employeeId);
    List<CollectionDto> getByQueryParamMonth(Long productId, Long employeeId);
    List<CollectionDto> getByQueryParamWeek(Long productId, Long employeeId);
    boolean deleteById(Long id);
}
