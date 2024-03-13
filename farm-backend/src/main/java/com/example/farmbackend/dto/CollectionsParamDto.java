package com.example.farmbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CollectionsParamDto {
    private final String employeeEmail;
    private final String type;
    private final double amount;
}
