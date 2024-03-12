package com.example.farmbackend.auth;



import com.example.farmbackend.config.JwtService;
import com.example.farmbackend.models.Employee;
import com.example.farmbackend.models.Role;
import com.example.farmbackend.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService  {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
    public AuthenticationResponse register(RegisterRequest request) {
        var user = Employee
                .builder()
                .firstName(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
