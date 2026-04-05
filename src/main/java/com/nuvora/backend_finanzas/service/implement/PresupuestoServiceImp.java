package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.PresupuestoService;

public class PresupuestoServiceImp implements PresupuestoService {

    @Override
    public BudgetDTO.Response generarPresupuesto(BudgetDTO.Request request, Usuario usuario){

        double ingreso = request.getIngreso();

        // Datos esenciales (si no vienen, se calculan)
        double esenciales = request.getGastosFijos() != null
                ? request.getDeudas()
                : ingreso * 0.5;

        // Deudas
        double deudas = request.getDeudas() != null
                ? request.getDeudas()
                : ingreso * 0.2;
        // Ahorro
        double ahorrro = request.getAhorroDeseado() != null
                ?request.getAhorroDeseado()
                : ingreso * 0.1;

        //Dinero restante
        double restante = ingreso - (esenciales + deudas + ahorrro);

        if (restante < 0){
            throw new RuntimeException("Los gastos superan su ingreso disponible");
        }

        //Distribucion dinero restante
        double imprevistos = restante * 0.2;
        double estiloVida = restante * 0.8;

        //Semanal
        double semanal = estiloVida / 4;

        //Respuesta
        BudgetDTO.Response response = new BudgetDTO.Response();
        response.setEsenciales(esenciales);
        response.setDeudas(deudas);
        response.setAhorro(ahorrro);
        response.setEstiloVida(estiloVida);
        response.setImprevistos(imprevistos);
        response.setDisponibleSemanal(semanal);

        return response;
    }
}
