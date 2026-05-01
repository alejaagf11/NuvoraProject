package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.LeccionDTO;
import com.nuvora.backend_finanzas.service.LeccionService;
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

    @PostMapping("/register")
    public ResponseEntity<LeccionDTO> createLeccion(@RequestBody LeccionDTO leccionDTO){
        return ResponseEntity.ok(leccionService.createLeccion(leccionDTO));

    }

    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<LeccionDTO>> listLeccionByModulo(@PathVariable Long moduloId){
        return ResponseEntity.ok(leccionService.listLeccionByModulo(moduloId));
    }

    @GetMapping("/{leccionId}")
    public ResponseEntity<LeccionDTO> getLeccionById(@PathVariable Long leccionId){
        return ResponseEntity.ok(leccionService.getLeccionById(leccionId));
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
}
