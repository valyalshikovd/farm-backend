package com.example.farmbackend.controller;


import com.example.farmbackend.auth.AuthenticationRequest;
import com.example.farmbackend.auth.AuthenticationResponse;
import com.example.farmbackend.auth.AuthenticationService;
import com.example.farmbackend.auth.RegisterRequest;
import com.example.farmbackend.models.Role;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

/**
Контроллер, отвечающий за регистрацию и аутентификацию пользователей.
@author Дмитрий Валяльщиков
 */
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    /**
     Сервис аутентификации.
     */
    private final AuthenticationService service;

    /**
     Регистрирует нового пользователя.
     @param request Данные регистрации пользователя.
     @param authentication Данные аутентификации текущего пользователя.
     @return Ответ HTTP-запроса со статусом 201 (Created) при успешной регистрации,
     или статусом 403 (Forbidden), если текущий пользователь не имеет прав администратора.
     */
    @PostMapping("/register")
    public ResponseEntity register(
            @RequestBody RegisterRequest request,
            Authentication authentication
    ){
        if(authentication == null || !RolesCheck.authoritiesCheck(authentication, Role.ADMIN.toString())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return service.register(request);
    }
    /**
     Аутентифицирует пользователя.
     @param request Данные аутентификации пользователя.
     @return Ответ HTTP-запроса со статусом 200 (OK) и данными аутентифицированного пользователя,
     или статусом 401 (Unauthorized) при ошибке аутентификации.
     */

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
