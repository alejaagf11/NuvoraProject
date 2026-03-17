package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.service.MetasAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metasAhorro")
public class MetasAhorroController {

    @Autowired
    private MetasAhorroService metasAhorroService;

    @PostMapping("/register")
    public ResponseEntity<?> createMeta(@RequestBody MetasAhorro metasAhorro){
        MetasAhorro newMetaAhorro = metasAhorroService.createMeta(metasAhorro);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMetaAhorro);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MetasAhorro>> listMeta(){
        List<MetasAhorro> metasAhorros = metasAhorroService.listMeta();
        return ResponseEntity.ok(metasAhorros);
    }

    @GetMapping("/list/{metaAhorroId}")
    public ResponseEntity<MetasAhorro> getMetaById(@PathVariable Long metaAhorroId){
        MetasAhorro metasAhorro = metasAhorroService.getMetaById(metaAhorroId);
        return ResponseEntity.ok(metasAhorro);
    }

    @PutMapping("/update/{metaAhorroId}")
    public ResponseEntity<?> updateMeta(@PathVariable Long metaAhorroId, @RequestBody MetasAhorro metasAhorro){
        try{
            MetasAhorro metasAhorrodb = metasAhorroService.updateMeta(metaAhorroId, metasAhorro);
            return ResponseEntity.ok(metasAhorrodb);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{metaAhorroId}")
        public ResponseEntity deleteMeta (@PathVariable Long metaAhorroId){
        try{
            metasAhorroService.deleteMeta(metaAhorroId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        }
    }

}
