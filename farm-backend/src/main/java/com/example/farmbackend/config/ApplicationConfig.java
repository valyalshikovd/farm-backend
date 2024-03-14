package com.example.farmbackend.config;


import com.example.farmbackend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс Spring Security для приложения.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private  final EmployeeRepository repository;

    /**
     * Создает сервис для загрузки данных пользователя по его имени.
     *
     * @return Реализация интерфейса UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findByEmail(username);
    }

    /**
     * Создает менеджер аутентификации на основе конфигурации Spring Security.
     *
     * @param config Конфигурация Spring Security.
     * @return Менеджер аутентификации.
     * @throws Exception - в случае ошибки при создании менеджера аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создает поставщика аутентификации на основе сервиса загрузки данных пользователя
     * и кодировщика паролей.
     *
     * @return Поставщик аутентификации.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider  = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Создает кодировщик паролей с использованием алгоритма BCrypt.
     *
     * @return Кодировщик паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
