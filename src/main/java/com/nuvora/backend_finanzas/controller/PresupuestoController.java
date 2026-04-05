package com.nuvora.backend_finanzas.controller;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.PresupuestoService;
import com.nuvora.backend_finanzas.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Usuario getUsuarioAutenticado(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            throw new RuntimeException("Usuario no autenticado");
        }
        return usuarioService.getUsuarioEntityById(userId);
    }

    @PostMapping("/generate")
    public BudgetDTO.Response generarPresupuesto(@RequestBody BudgetDTO.Request request, HttpServletRequest req){
        Usuario usuario = getUsuarioAutenticado(req);
        return presupuestoService.generarPresupuesto(request, usuario);
    }

}
