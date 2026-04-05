package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface PresupuestoService {
    BudgetDTO.Response generarPresupuesto(BudgetDTO.Request request, Usuario usuario);
}
