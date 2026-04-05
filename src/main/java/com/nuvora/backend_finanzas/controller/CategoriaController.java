package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.CategoriaDTO;
import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.CategoriaService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/register")
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO){
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(categoriaService.createCategoria(categoriaDTO, usuario));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoriaDTO>> listCategoria(){
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(categoriaService.listCategoria(usuario));
    }

    @PutMapping("/update/{categoriaId}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaDTO categoriaDTO) throws Exception{
        Usuario usuario = getUsuarioAutenticado();
        return ResponseEntity.ok(categoriaService.updateCategoria(categoriaId, categoriaDTO, usuario));
    }

    @DeleteMapping("/delete/{categoriaId}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long categoriaId) throws Exception{

        try {
            Usuario usuario = getUsuarioAutenticado();
            categoriaService.deleteCategoria(categoriaId, usuario);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }
}
