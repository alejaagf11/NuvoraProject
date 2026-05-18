package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetGenerateAiResponseDTO {

    private BudgetDTO.Response budget;
    private String resumen;
    private List<String> recomendaciones;
    private List<String> alertas;
    private String source;
}
