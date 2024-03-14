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

/**
 * Сервис для управления данными сотрудников.
 *
 * @author Дмитрий Валяльщиков
 */
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * Репозиторий для работы с данными сотрудников.
     */
    private EmployeeRepository employeeRepository;

    /**
     * Создает нового сотрудника в базе данных.
     *
     * @param employeeDto DTO объект с данными о сотруднике.
     * @return DTO объект созданного сотрудника.
     */
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee  = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    /**
     * Получает сотрудника по его идентификатору.
     *
     * @param employeeId Идентификатор сотрудника.
     * @return DTO объект найденного сотрудника.
     * @throws NotFoundException - если сотрудник не найден.
     */
    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow( () ->
                        new NotFoundException("Employee is not exists with given id : " + employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    /**
     * Получает сотрудника по его email.
     *
     * @param name Email сотрудника.
     * @return DTO объект найденного сотрудника, или null, если сотрудник не найден.
     */
    @Override
    public EmployeeDto getEmployeeByName(String name) {
        Employee employee = employeeRepository.findByEmail(name);
        if(employee == null){
            return null;
        }
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    /**
     * Получает список всех сотрудников.
     *
     * @return Список DTO объектов всех сотрудников.
     */
    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> listEmployers = employeeRepository.findAll();
        return listEmployers.stream()
                .map(  (employee) ->  EmployeeMapper.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    /**
     * Обновляет данные сотрудника.
     *
     * @param employeeId Идентификатор сотрудника для обновления.
     * @param updatedEmployee DTO объект с обновленными данными сотрудника.
     * @return DTO объект обновленного сотрудника.
     * @throws NotFoundException - если сотрудник не найден.
     */
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

    /**
     * Удаляет сотрудника по его идентификатору.
     *
     * @param employeeId Идентификатор сотрудника для удаления.
     * @throws NotFoundException - если сотрудник не найден.
     */
    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new NotFoundException("Employee is not exists with given id : " + employeeId)
                );
        employeeRepository.deleteById(employeeId);
    }
}
