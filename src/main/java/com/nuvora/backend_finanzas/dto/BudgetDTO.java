package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BudgetDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
       private Double ingreso;
        private Double deudas; // puede ser null si no tiene
        private Double ahorroDeseado; // opcional
        private String estiloVida; // "estricto", "flexible" o null
        private String tipoIngreso; // "semanal", "quincenal"
        private List<GastoFijoDTO> gastosFijos;
        private List<GastoFijoDTO> gastosVariables;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private double esenciales;
        private double variables;
        private double deudas;
        private double ahorro;
        private double estiloVida;
        private double imprevistos;
        private double disponibleSemanal;
        private Map<String, Double> disponibleQuincena;
    }
}