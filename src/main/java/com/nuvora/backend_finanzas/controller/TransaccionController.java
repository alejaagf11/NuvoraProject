package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.TransaccionDTO;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.TransaccionService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<TransaccionDTO> createTransaccion(@RequestBody TransaccionDTO transaccionDTO){
        Usuario usuario = getUsuarioAutenticado();
        TransaccionDTO created = transaccionService.createTransaccion(transaccionDTO, usuario);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransaccionDTO>> listTransaccion(){
        Usuario usuario = getUsuarioAutenticado();
        List<TransaccionDTO> transacciones = transaccionService.listTransaccion(usuario);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransaccionDTO>> filterByType(@PathVariable String tipo){
        Usuario usuario = getUsuarioAutenticado();
        List<TransaccionDTO> transacciones = transaccionService.filterByType(tipo, usuario);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity <List<TransaccionDTO>> filterByCategoria(@PathVariable Long categoriaId){
        Usuario usuario = getUsuarioAutenticado();
        List<TransaccionDTO> transacciones = transaccionService.filterByCategoria(categoriaId, usuario);
        return ResponseEntity.ok(transacciones);

    }

    @PutMapping("/update/{transaccionId}")
    public ResponseEntity<TransaccionDTO> updateTransaccion(@PathVariable Long transaccionId, @RequestBody TransaccionDTO transaccionDTO){
        Usuario usuario = getUsuarioAutenticado();
        TransaccionDTO update = transaccionService.updateTransaccion(transaccionId, transaccionDTO, usuario);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{transaccionId}")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long transaccionId){

        try {
            Usuario usuario = getUsuarioAutenticado();

            transaccionService.deleteTransaccion(transaccionId, usuario);

            return ResponseEntity.noContent().build();

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    @PutMapping("/saldo")
    public ResponseEntity<Double> calcularSaldoUsuario(){
        Usuario usuario = getUsuarioAutenticado();
        double saldo = transaccionService.calcularSaldoUsuario(usuario);
        return ResponseEntity.ok(saldo);
    }
}
