package com.example.farmbackend.controller;


import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.models.Role;
import com.example.farmbackend.service.EmployeeService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @PathVariable("id") Long employeeId,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployeeDto(Authentication authentication){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable("id") Long employeeId,
            @RequestBody EmployeeDto updateEmployee,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updateEmployee);
        return  ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable("id")  Long employeeId,
            Authentication authentication
    ){
        if(!checkAdminRole(authentication)){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        employeeService.deleteEmployee(employeeId);
        return  ResponseEntity.ok("Employee deleted successfully!");
    }
}
