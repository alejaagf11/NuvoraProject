package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.PresupuestoService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presupuesto")
public class PresupuestoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PresupuestoService presupuestoService;

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/generate")
    public BudgetDTO.Response generarPresupuesto(@RequestBody BudgetDTO.Request request){
        Usuario usuario = getUsuarioAutenticado();
        return presupuestoService.generarPresupuesto(request, usuario);
    }

}
