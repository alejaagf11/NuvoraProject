package com.nuvora.backend_finanzas.config;

import com.nuvora.backend_finanzas.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        try {
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7); // quitar "Bearer "
                Long userId = jwtService.validateTokenAndGetUserId(token);

                // Crear autenticación y colocar en SecurityContext
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("JwtFilter: Usuario autenticado con ID = " + userId);
            }
        } catch (Exception e) {
            // Token inválido → 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Muy importante: continuar la cadena de filtros
        filterChain.doFilter(request, response);
    }
}