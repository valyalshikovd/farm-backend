package com.example.farmbackend.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.lang.NonNull;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CollectionDto {
    private Long id;
    private EmployeeDto employeeDto;
    private ProductDto productDto;
    private double amount;
    private Date dateCreating;
}
