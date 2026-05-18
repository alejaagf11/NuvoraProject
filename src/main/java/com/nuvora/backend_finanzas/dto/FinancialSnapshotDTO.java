package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialSnapshotDTO {

    private Long usuarioId;
    private Double montoMensualUsuario;
    private Double totalIngresos;
    private Double totalGastos;
    private Double totalDeudasAhorro;
    private Double totalAhorradoMetas;
    private Double totalObjetivoMetas;
    private Double saldoEstimado;
    private Map<String, Double> gastosPorCategoria;
    private String resumenGeneral;
}

