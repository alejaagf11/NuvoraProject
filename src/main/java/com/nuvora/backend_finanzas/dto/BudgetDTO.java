package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BudgetDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Double ingreso;
        private Double gastosFijos;
        private Double deudas;
        private Double ahorroDeseado;
        private String estiloVida;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Double esenciales;
        private Double deudas;
        private Double ahorro;
        private Double estiloVida;
        private Double imprevistos;
        private Double disponibleSemanal;
    }
}