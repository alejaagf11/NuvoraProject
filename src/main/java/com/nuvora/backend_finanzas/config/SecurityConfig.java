package com.nuvora.backend_finanzas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Permite usar BCrypt en todo el proyecto
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()          // register / login
                        .requestMatchers("/api/metasAhorro/**").permitAll()  // CRUD MetasAhorro sin auth

                        // Cualquier otro endpoint requiere autenticación (por si agregas más adelante)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // Desactiva login por formulario

        return http.build();
    }
}
