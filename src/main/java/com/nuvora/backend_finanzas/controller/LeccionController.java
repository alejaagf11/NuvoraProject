package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.LeccionDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.LeccionService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecciones")
public class LeccionController {

    @Autowired
    private LeccionService leccionService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<LeccionDTO> createLeccion(@RequestBody LeccionDTO leccionDTO){
        return ResponseEntity.ok(leccionService.createLeccion(leccionDTO));

    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<LeccionDTO>> listLeccionByModulo(@PathVariable Long moduloId){
        return ResponseEntity.ok(leccionService.listLeccionByModulo(moduloId));
    }

    @GetMapping("/{leccionId}")
    public ResponseEntity<LeccionDTO> getLeccionById(@PathVariable Long leccionId, HttpServletRequest request){

        try{
            Usuario usuario = getUsuarioAutenticado(request);
            return ResponseEntity.ok(leccionService.getLeccionByIdParaUsuario(leccionId,usuario));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @PutMapping("/update/{leccionId}")
    public ResponseEntity<?> updateLeccion(@PathVariable Long leccionId, @RequestBody LeccionDTO leccionDTO){
        try{
            return ResponseEntity.ok(leccionService.updateLeccion(leccionId, leccionDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{leccionId}")
    public ResponseEntity<Void> deleteLeccion(@PathVariable Long leccionId){
        try{
            leccionService.deleteLeccion(leccionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Usuario getUsuarioAutenticado(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        return usuarioService.getUsuarioEntityById(userId);
    }
}
