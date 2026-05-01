package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.dto.ProgresoLeccionUsuarioDTO;
import com.nuvora.backend_finanzas.entity.ProgresoLeccionUsuario;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.ProgresoLeccionUsuarioService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public class ProgresoLeccionUsuarioController {

    @Autowired
    private ProgresoLeccionUsuarioService progresoLeccionUsuarioService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario getUsuarioAutenticado(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            throw new RuntimeException("Usuario no autenticado");
        }
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/completar/{leccionId}")
    public ResponseEntity<ProgresoLeccionUsuarioDTO> completarLeccion(@PathVariable Long leccionId, HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(progresoLeccionUsuarioService.completarLeccion(leccionId, usuario));
    }

    @GetMapping("/mis-lecciones")
    public ResponseEntity<List<ProgresoLeccionUsuarioDTO>> listProgresoUsuario(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(progresoLeccionUsuarioService.listProgresoUsuario(usuario));
    }

    @GetMapping("/modulos")
    public ResponseEntity<List<ModuloProgresoResumDTO>> getProgresoModulos(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(progresoLeccionUsuarioService.getProgresoModulos(usuario));
    }
}
