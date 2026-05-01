package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.ModuloAprendizajeDTO;
import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.ModuloAprendizajeService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloAprencizajeController {

    @Autowired
    private ModuloAprendizajeService moduloAprendizajeService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario getUsuarioAutenticado(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            throw new RuntimeException("Usuario no autenticado");
        }
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<ModuloAprendizajeDTO> createModulo(@RequestBody ModuloAprendizajeDTO moduloDTO){
        return ResponseEntity.ok(moduloAprendizajeService.createModulo(moduloDTO));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ModuloAprendizajeDTO>> listModulos(){
        return ResponseEntity.ok(moduloAprendizajeService.listModulos());
    }

    @GetMapping("/progreso")
    public ResponseEntity<List<ModuloProgresoResumDTO>>listModuloProgreso(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(moduloAprendizajeService.listModuloProgreso(usuario));
    }

    @GetMapping("/{moduloId}")
    public ResponseEntity<ModuloAprendizajeDTO> getModuloById(@PathVariable Long moduloId){
        return ResponseEntity.ok(moduloAprendizajeService.getModuloById(moduloId));
    }

    @PutMapping("/update/{moduloId}")
    public ResponseEntity<?> updateModulo(@PathVariable Long moduloId, @RequestBody ModuloAprendizajeDTO moduloDTO){
        try{
            return ResponseEntity.ok(moduloAprendizajeService.updateModulo(moduloId, moduloDTO));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{moduloId}")
    public ResponseEntity<Void> deleteModulo(@PathVariable Long moduloId){
        try{
            moduloAprendizajeService.deleteModulo(moduloId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
