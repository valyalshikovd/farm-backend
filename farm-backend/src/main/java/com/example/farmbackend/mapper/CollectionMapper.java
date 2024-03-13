package com.example.farmbackend.mapper;

import com.example.farmbackend.dto.CollectionDto;
import com.example.farmbackend.models.Collection;

import java.util.Date;

public class CollectionMapper {

    public static Collection mapToCollect(CollectionDto collectionDto){

        Collection collection = Collection
                .builder()
                .productId(collectionDto.getProductDto().getId())
                .employeeId(collectionDto.getEmployeeDto().getId())
                .amount(collectionDto.getAmount())
                .dateCreating(new Date())
                .build();
        return collection;
    }


}
