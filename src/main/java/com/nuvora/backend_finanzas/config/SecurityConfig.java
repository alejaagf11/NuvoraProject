package com.nuvora.backend_finanzas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()          // register / login
                        .requestMatchers("/api/metasAhorro/**").permitAll()
                        .requestMatchers("/api/usuario/**").permitAll()// CRUD MetasAhorro sin auth

                        // Cualquier otro endpoint requiere autenticación (por si agregas más adelante)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // Desactiva login por formulario

        return http.build();
    }

    // config CORS Angular
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
