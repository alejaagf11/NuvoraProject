package com.nuvora.backend_finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuloProgresoResumDTO {
    private Long moduloId;
    private String tituloModulo;
    private String descripcionModulo;
    private Integer ordenModulo;
    private Integer totalLecciones;
    private Integer leccionesCompletadas;
    private Double porcentajeProgreso;
    private Boolean desbloqueado;
}
