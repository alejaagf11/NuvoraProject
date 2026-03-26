package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.MetasAhorroService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Meta;
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
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> createMeta(@RequestBody MetasAhorro metasAhorro, HttpServletRequest request){
        long usuarioId = (Long) request.getAttribute("userId");
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        MetasAhorro newMetaAhorro = metasAhorroService.createMeta(metasAhorro, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMetaAhorro);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MetasAhorro>> listMeta(HttpServletRequest request){
        long usuarioId = (Long) request.getAttribute("userId");
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);

        List<MetasAhorro> metasAhorros = metasAhorroService.listMeta(usuario);
        return ResponseEntity.ok(metasAhorros);
    }

    @GetMapping("/list/{metaAhorroId}")
    public ResponseEntity<MetasAhorro> getMetaById(@PathVariable Long metaAhorroId,
                                                   HttpServletRequest request)throws Exception{

            Long usuarioId = (Long) request.getAttribute("userId");
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            MetasAhorro metasAhorro = metasAhorroService.getMetaById(metaAhorroId, usuario);

            return ResponseEntity.ok(metasAhorro);

    }

    @PutMapping("/update/{metaAhorroId}")
    public ResponseEntity<?> updateMeta(@PathVariable Long metaAhorroId, @RequestBody MetasAhorro metasAhorro, HttpServletRequest request){
        try{
            long usuarioId = (Long) request.getAttribute("userId");
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);

            MetasAhorro metasAhorrodb = metasAhorroService.updateMeta(metaAhorroId, metasAhorro, usuario);
            return ResponseEntity.ok(metasAhorrodb);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{metaAhorroId}")
        public ResponseEntity deleteMeta (@PathVariable Long metaAhorroId, HttpServletRequest request){

        try{
            long usuarioId = (Long) request.getAttribute("userId");
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);

            metasAhorroService.deleteMeta(metaAhorroId, usuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        }
    }

    @PutMapping("/abonar/{metaAhorroId}")
    public ResponseEntity<MetasAhorro> abonar(@PathVariable Long metaAhorroId,
                                              @RequestParam Double monto,
                                              HttpServletRequest request) throws Exception{

        Long usuarioId = (Long) request.getAttribute("userId");
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);

        return ResponseEntity.ok(
                metasAhorroService.abonar(metaAhorroId, monto, usuario)
        );
    }

}
