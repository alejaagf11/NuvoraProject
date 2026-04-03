package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.UsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    private Usuario getUsuarioAutenticado(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return usuarioService.getUsuarioById(userId).toEntity();
    }


    @GetMapping("/list")
    public ResponseEntity<List<UsuarioDTO>> listUsuario() {
        List<UsuarioDTO> usuarios = usuarioService.listUsuario();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getMiUsuario(HttpServletRequest request) {
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUsuario(@RequestBody UsuarioDTO datosNuevos, HttpServletRequest request) {
        try {
            Usuario usuarioAutenticado = getUsuarioAutenticado(request);

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
    public ResponseEntity<?> deleteMiUsuario(HttpServletRequest request) {
        try {
            Usuario usuario = getUsuarioAutenticado(request);
            usuarioService.deleteUsuario(usuario);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}