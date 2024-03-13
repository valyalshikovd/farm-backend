package com.example.farmbackend.controller;

import com.example.farmbackend.models.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class ControllerUtils {

    static boolean checkAdminRole( Authentication authentication){
        if(authentication == null || !RolesCheck.authoritiesCheck(authentication, Role.ADMIN.toString())) {
            return false;
        }
        return true;
    }

    static boolean checkEmployeeRole( Authentication authentication){
        if(authentication == null
                || (!RolesCheck.authoritiesCheck(authentication, Role.EMPLOYEE.toString())
                && !RolesCheck.authoritiesCheck(authentication, Role.ADMIN.toString()))) {
            return false;
        }
        return true;
    }
}
