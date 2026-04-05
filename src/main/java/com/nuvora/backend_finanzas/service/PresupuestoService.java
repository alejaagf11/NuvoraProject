package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.entity.Usuario;

public interface PresupuestoService {
    BudgetDTO.Response generarPresupuesto(BudgetDTO.Request request, Usuario usuario);
}
