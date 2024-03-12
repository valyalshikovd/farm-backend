package com.example.farmbackend.service.impl;

import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.exception.NotFoundException;
import com.example.farmbackend.mapper.EmployeeMapper;
import com.example.farmbackend.models.Employee;
import com.example.farmbackend.repository.EmployeeRepository;
import com.example.farmbackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee  = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);

    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow( () ->
                        new NotFoundException("Employee is not exists with given id : " + employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> listEmployers = employeeRepository.findAll();
        return listEmployers.stream()
                .map(  (employee) ->  EmployeeMapper.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new NotFoundException("Employee is not exists with given id : " + employeeId)
                );
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastname(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setPassword(updatedEmployee.getPassword());

        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new NotFoundException("Employee is not exists with given id : " + employeeId)
                );
        ///
        employeeRepository.deleteById(employeeId);
    }
}
