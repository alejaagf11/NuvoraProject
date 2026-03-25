package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/list")
    public ResponseEntity<List<Usuario>> listUsuario(){
        List<Usuario> usuarios = usuarioService.listUsuario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/list/{usuarioId}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long usuarioId){
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/update/{usuarioId}")
    public ResponseEntity<?> updateUsuario ( @PathVariable Long usuarioId, @RequestBody Usuario usuario) {
        try{
            Usuario usuariodb = usuarioService.updateUsuario(usuarioId, usuario);
            return ResponseEntity.ok(usuariodb);
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/delete/{usuarioId}")
    public ResponseEntity deleteUsuario (@PathVariable Long usuarioId){
        try {
            usuarioService.deleteUsuario(usuarioId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        }
    }


}
