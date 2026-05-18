package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetAiResponseDTO {

    private String resumen;
    private Double esenciales;
    private Double variables;
    private Double deudas;
    private Double ahorro;
    private Double imprevistos;
    private Double estiloVida;
    private Double disponibleSemanal;
    private Map<String, Double> disponibleQuincena;
    private List<String> recomendaciones;
    private List<String> alertas;
}
