package com.example.farmbackend.auth;



import com.example.farmbackend.config.JwtService;
import com.example.farmbackend.models.Employee;
import com.example.farmbackend.models.Role;
import com.example.farmbackend.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Сервис для аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService  {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Создает учетную запись администратора после инициализации сервиса.
     */
    @PostConstruct
    private void addAdminUser(){
        var user = Employee
                .builder()
                .firstName("ADMIN_NAME")
                .lastname("ADMIN_LASTNAME")
                .email("ADMIN_EMAIL")
                .password(passwordEncoder.encode("0000"))
                .role(Role.ADMIN)
                .build();

        repository.save(user);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param request DTO объект с данными для регистрации.
     * @return HTTP ответ с кодом 200 OK, если регистрация прошла успешно.
     */
    public ResponseEntity register(RegisterRequest request) {
        var user = Employee
                .builder()
                .firstName(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .build();

        repository.save(user);
        return ResponseEntity.ok().build();
    }


    /**
     * Аутентифицирует пользователя и возвращает JWT токен.
     *
     * @param request DTO объект с данными для аутентификации.
     * @return DTO объект с JWT токеном, если аутентификация прошла успешно.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
