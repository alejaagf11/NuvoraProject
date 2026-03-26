package com.nuvora.backend_finanzas.controller;

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


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@Valid @RequestBody Usuario usuario){
        Usuario newUsuario = usuarioService.registerUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@Valid @RequestBody Usuario usuario){
        Usuario usuarioLogin = usuarioService.loginUsuario(
                usuario.getCorreoUsuario(),
                usuario.getContrasenaUsuario()
        );

        if(usuarioLogin != null){
            String token = jwtService.createToken(usuarioLogin.getUsuarioId());
            return ResponseEntity.ok(new LoginResponse(token));
        }

        return ResponseEntity.status(401).body("Credenciales Incorrectas");
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
