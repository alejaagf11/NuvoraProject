package com.nuvora.backend_finanzas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/usuario/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/modulos/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/modulos/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/modulos/delete/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/lecciones/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/lecciones/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/lecciones/delete/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/modulos/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/lecciones/**").authenticated()
                        .requestMatchers("/api/progreso/**").authenticated()

                        .requestMatchers("/api/metasAhorro/**").authenticated()
                        .requestMatchers("/api/transacciones/**").authenticated()
                        .requestMatchers("/api/categorias/**").authenticated()
                        .requestMatchers("/api/presupuesto/**").authenticated()
                        .requestMatchers("/api/usuario/me").authenticated()
                        .requestMatchers("/api/usuario/update").authenticated()
                        .requestMatchers("/api/usuario/delete").authenticated()
                        // Cualquier otro requiere autenticación
                        .anyRequest().authenticated()
                )
                // Agregar JwtFilter antes de UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable()); // desactivar login por formulario

        return http.build();
    }

    // Configuración CORS Angular
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "http://localhost:4200",
                                "http://localhost",
                                "capacitor://localhost",
                                "http://localhost:*",
                                "capacitor://localhost:*"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}