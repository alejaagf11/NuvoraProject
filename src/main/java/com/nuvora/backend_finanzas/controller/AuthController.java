package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.UsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.security.JwtService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> registerUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO){
        UsuarioDTO newUsuario = usuarioService.registerUsuario(usuarioDTO);

        System.out.println("REGISTER HIT");
        return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, HttpServletRequest request) {
        UsuarioDTO usuarioLogin = usuarioService.loginUsuario(
                usuarioDTO.getCorreoUsuario(),
                usuarioDTO.getContrasenaUsuario()
        );

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String tokenActual = header.substring(7);
                Long usuarioActualId = jwtService.validateTokenAndGetUserId(tokenActual);
                Usuario usuarioActual = usuarioService.getUsuarioEntityById(usuarioActualId);

                boolean actualEsAdmin = "ADMIN".equalsIgnoreCase(usuarioActual.getRolUsuario());
                boolean nuevoEsAdmin = "ADMIN".equalsIgnoreCase(usuarioLogin.getRolUsuario());
                boolean esOtroUsuario = !usuarioActual.getUsuarioId().equals(usuarioLogin.getUsuarioId());

                if (actualEsAdmin && nuevoEsAdmin && esOtroUsuario) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Ya hay una cuenta de administrador abierta en este navegador");
                }
            } catch (Exception ignored) {
                // Si el token anterior está vencido o inválido, dejamos continuar el login normal.
            }
        }

        String token = jwtService.createToken(usuarioLogin.getUsuarioId());
        return ResponseEntity.ok(new LoginResponse(token));
    }


    public static class LoginResponse{
        private String token;

        public LoginResponse(String token){
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token){
            this.token = token;
        }
    }
}
