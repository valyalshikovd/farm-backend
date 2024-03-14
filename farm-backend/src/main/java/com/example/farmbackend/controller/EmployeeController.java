package com.example.farmbackend.controller;

import com.example.farmbackend.dto.EmployeeDto;
import com.example.farmbackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.farmbackend.controller.ControllerUtils.checkAdminRole;

/**
 * Контроллер, отвечающий за управление сотрудниками.
 *
 * @author Дмитрий Валяльщиков
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    /**
     * Получает сотрудника по его идентификатору.
     *
     * @param employeeId Идентификатор сотрудника.
     * @param authentication Данные аутентификации текущего пользователя.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и данными сотрудника,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если сотрудник не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId, Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employeeDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Получает список всех сотрудников.
     *
     * @param authentication Данные аутентификации текущего пользователя.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и списком всех сотрудников,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если сотрудники не найдены.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployeeDto(Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            List<EmployeeDto> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Обновляет информацию о сотруднике.
     *
     * @param employeeId Идентификатор сотрудника.
     * @param updateEmployee Объект с обновленными данными сотрудника.
     * @param authentication Данные аутентификации текущего пользователя.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и обновленными данными сотрудника,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если сотрудник не найден.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDto updateEmployee, Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updateEmployee);
            return ResponseEntity.ok(employeeDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Удаляет сотрудника.
     *
     * @param employeeId Идентификатор сотрудника.
     * @param authentication Данные аутентификации текущего пользователя.
     * @return Ответ HTTP-запроса со статусом 200 (OK) и сообщением об успешном удалении,
     *         или статус 403 (Forbidden), если текущий пользователь не имеет прав администратора,
     *         или статус 404 (Not Found), если сотрудник не найден.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId, Authentication authentication) {
        if (!checkAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try{
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
