package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.UsuarioService;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@RequestBody Usuario usuario){
        Usuario newUsuario = usuarioService.registerUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario){
        Usuario usuarioLogin = usuarioService.loginUsuario(
                usuario.getCorreoUsuario(),
                usuario.getContrasenaUsuario()
        );

        return ResponseEntity.ok(usuarioLogin);
    }
}
