package com.example.farmbackend.mapper;

import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.models.Employee;
public class EmployeeMapper {

    /**
     * Класс для преобразования объекта-модели в Data-transfer-Object
     */
    public static EmployeeDto mapToEmployeeDto(Employee employee){
        return new EmployeeDto(employee.getId(),
                employee.getFirstName(),
                employee.getLastname(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getRole());
    }
    public static Employee mapToEmployee(EmployeeDto employeeDto){
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getPassword(),
                employeeDto.getRole()
        );
    }
}
