package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetasAhorroDTO {

    private Long metaAhorroId;
    private String nombreMeta;
    private Double montoObjetivo;
    private Double montoAhorrado;
    private Double ahorroMensual;
    private LocalDate fechaLimite;
    private Double montoRestante;

    //  ENTITY → DTO
    public static MetasAhorroDTO fromEntity(MetasAhorro meta){
        return new MetasAhorroDTO(
                meta.getMetaAhorroId(),
                meta.getNombreMeta(),
                meta.getMontoObjetivo(),
                meta.getMontoAhorrado(),
                meta.getAhorroMensual(),
                meta.getFechaLimite(),
                meta.getMontoRestante()
        );
    }

    // DTO → ENTITY
    public MetasAhorro toEntity(){
        MetasAhorro meta = new MetasAhorro();
        meta.setMetaAhorroId(this.metaAhorroId);
        meta.setNombreMeta(this.nombreMeta);
        meta.setMontoObjetivo(this.montoObjetivo);
        meta.setFechaLimite(this.fechaLimite);
        return meta;
    }
}