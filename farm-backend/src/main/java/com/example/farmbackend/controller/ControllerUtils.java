package com.example.farmbackend.controller;

import com.example.farmbackend.models.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

/**
 * Класс, содержащий методы для проверки ролей пользователей.
 * @author ВаляльщиковД Дмитрий
 */
public class ControllerUtils {

    /**
     * Проверяет, является ли сотрудник администратором.
     *
     * @param authentication Объект аутентификации пользователя.
     * @return True, если пользователь имеет роль администратора, false - в другом случае.
     */
    static boolean checkAdminRole( Authentication authentication){
        if(authentication == null || !RolesCheck.authoritiesCheck(authentication, Role.ADMIN.toString())) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет, является ли пользователь сотрудником (включая роль администратора,
     * которая наследует права сотрудника).
     *
     * @param authentication Объект аутентификации пользователя.
     * @return True, если пользователь имеет роль сотрудника или администратора, false - в другом случае.
     */
    static boolean checkEmployeeRole( Authentication authentication){
        if(authentication == null
                || (!RolesCheck.authoritiesCheck(authentication, Role.EMPLOYEE.toString())
                && !RolesCheck.authoritiesCheck(authentication, Role.ADMIN.toString()))) {
            return false;
        }
        return true;
    }
}
