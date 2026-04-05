package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.UsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        return usuarioService.getUsuarioEntityById(userId);
    }


    @GetMapping("/list")
    public ResponseEntity<List<UsuarioDTO>> listUsuario() {
        List<UsuarioDTO> usuarios = usuarioService.listUsuario();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getMiUsuario(HttpServletRequest request) {
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUsuario(@RequestBody UsuarioDTO datosNuevos, HttpServletRequest request) {
        try {
            Usuario usuarioAutenticado = getUsuarioAutenticado();

            UsuarioDTO actualizado = usuarioService.updateUsuario(
                    usuarioAutenticado,
                    datosNuevos
            );
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMiUsuario(HttpServletRequest request) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            usuarioService.deleteUsuario(usuario);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}