package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.MetasAhorroDTO;
import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.MetasAhorroService;
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
@RequestMapping("/api/metasAhorro")
public class MetasAhorroController {

    @Autowired
    private MetasAhorroService metasAhorroService;
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
    public ResponseEntity<MetasAhorroDTO> createMeta(@RequestBody MetasAhorroDTO metasAhorroDTO, HttpServletRequest request){

        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(
                metasAhorroService.createMeta(metasAhorroDTO, usuario));

    }

    @GetMapping("/list")
    public ResponseEntity<List<MetasAhorroDTO>> listMeta(HttpServletRequest request){
        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(
                metasAhorroService.listMeta(usuario));
    }

    @GetMapping("/list/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> getMetaById(@PathVariable Long metaAhorroId, HttpServletRequest request)throws Exception{

        Usuario usuario = getUsuarioAutenticado(request);
        return ResponseEntity.ok(
                metasAhorroService.getMetaById(metaAhorroId,usuario));

    }

    @PutMapping("/update/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> updateMeta(@PathVariable Long metaAhorroId, @RequestBody MetasAhorroDTO metasAhorroDTO, HttpServletRequest request){
        try{
            Usuario usuario = getUsuarioAutenticado(request);
            return ResponseEntity.ok(metasAhorroService.updateMeta(metaAhorroId, metasAhorroDTO, usuario));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{metaAhorroId}")
        public ResponseEntity<Void> deleteMeta (@PathVariable Long metaAhorroId, HttpServletRequest request){

        try{
            Usuario usuario = getUsuarioAutenticado(request);

            metasAhorroService.deleteMeta(metaAhorroId, usuario);

            return ResponseEntity.noContent().build();
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/abonar/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> abonar(@PathVariable Long metaAhorroId,
                                              @RequestParam Double monto, HttpServletRequest request) throws Exception{

        Usuario usuario = getUsuarioAutenticado(request);

        return ResponseEntity.ok(
                metasAhorroService.abonar(metaAhorroId, monto, usuario)
        );
    }

}
