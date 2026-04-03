package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.MetasAhorroDTO;
import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.MetasAhorroService;
import com.nuvora.backend_finanzas.service.UsuarioService;
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

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<MetasAhorroDTO> createMeta(@RequestBody MetasAhorroDTO metasAhorroDTO){

        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(
                metasAhorroService.createMeta(metasAhorroDTO, usuario));

    }

    @GetMapping("/list")
    public ResponseEntity<List<MetasAhorroDTO>> listMeta(){
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(
                metasAhorroService.listMeta(usuario));
    }

    @GetMapping("/list/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> getMetaById(@PathVariable Long metaAhorroId)throws Exception{

        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(
                metasAhorroService.getMetaById(metaAhorroId,usuario));

    }

    @PutMapping("/update/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> updateMeta(@PathVariable Long metaAhorroId, @RequestBody MetasAhorroDTO metasAhorroDTO){
        try{
            Usuario usuario = getUsuarioAutenticado();
            return ResponseEntity.ok(metasAhorroService.updateMeta(metaAhorroId, metasAhorroDTO, usuario));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{metaAhorroId}")
        public ResponseEntity deleteMeta (@PathVariable Long metaAhorroId){

        try{
            Usuario usuario = getUsuarioAutenticado();

            metasAhorroService.deleteMeta(metaAhorroId, usuario);

            return ResponseEntity.ok("Eliminado correctamente");
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping("/abonar/{metaAhorroId}")
    public ResponseEntity<MetasAhorroDTO> abonar(@PathVariable Long metaAhorroId,
                                              @RequestParam Double monto) throws Exception{

        Usuario usuario = getUsuarioAutenticado();

        return ResponseEntity.ok(
                metasAhorroService.abonar(metaAhorroId, monto, usuario)
        );
    }

}
