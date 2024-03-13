package com.example.farmbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Setter
@Getter
public class RequestCollection {
    private Long id;
    private String employeeEmail;
    private String type;
    private double amount;
}
