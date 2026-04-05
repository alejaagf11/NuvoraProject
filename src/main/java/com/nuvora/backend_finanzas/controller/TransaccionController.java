package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.TransaccionDTO;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.TransaccionService;
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
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

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
    public ResponseEntity<TransaccionDTO> createTransaccion(@RequestBody TransaccionDTO transaccionDTO, HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        TransaccionDTO created = transaccionService.createTransaccion(transaccionDTO, usuario);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransaccionDTO>> listTransaccion(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        List<TransaccionDTO> transacciones = transaccionService.listTransaccion(usuario);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransaccionDTO>> filterByType(@PathVariable String tipo, HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        List<TransaccionDTO> transacciones = transaccionService.filterByType(tipo, usuario);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity <List<TransaccionDTO>> filterByCategoria(@PathVariable Long categoriaId, HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        List<TransaccionDTO> transacciones = transaccionService.filterByCategoria(categoriaId, usuario);
        return ResponseEntity.ok(transacciones);

    }

    @PutMapping("/update/{transaccionId}")
    public ResponseEntity<TransaccionDTO> updateTransaccion(@PathVariable Long transaccionId, @RequestBody TransaccionDTO transaccionDTO, HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        TransaccionDTO update = transaccionService.updateTransaccion(transaccionId, transaccionDTO, usuario);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{transaccionId}")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long transaccionId, HttpServletRequest request){

        try {
            Usuario usuario = getUsuarioAutenticado(request);

            transaccionService.deleteTransaccion(transaccionId, usuario);

            return ResponseEntity.noContent().build();

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    @PutMapping("/saldo")
    public ResponseEntity<Double> calcularSaldoUsuario(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        double saldo = transaccionService.calcularSaldoUsuario(usuario);
        return ResponseEntity.ok(saldo);
    }
}
