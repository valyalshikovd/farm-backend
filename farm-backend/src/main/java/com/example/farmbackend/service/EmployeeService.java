package com.example.farmbackend.service;

import com.example.farmbackend.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);


    EmployeeDto getEmployeeById(Long employeeId);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee);

    EmployeeDto getEmployeeByName(String name);

    void deleteEmployee(Long employeeId);
}
