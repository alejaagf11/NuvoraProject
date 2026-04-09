package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    private Long mesesRestantes;

    //  ENTITY → DTO
    public static MetasAhorroDTO fromEntity(MetasAhorro meta){
        LocalDate hoy = LocalDate.now();
        long mesesRestantes = calcularMesesRestantes(hoy, meta.getFechaLimite());
        return new MetasAhorroDTO(
                meta.getMetaAhorroId(),
                meta.getNombreMeta(),
                meta.getMontoObjetivo(),
                meta.getMontoAhorrado(),
                meta.getAhorroMensual(),
                meta.getFechaLimite(),
                meta.getMontoRestante(),
                mesesRestantes
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

    private static long calcularMesesRestantes(LocalDate hoy, LocalDate fechaLimite) {
        if (fechaLimite == null || !fechaLimite.isAfter(hoy)) {
            return 0;
        }

        long meses = ChronoUnit.MONTHS.between(
                hoy.withDayOfMonth(1),
                fechaLimite.withDayOfMonth(1)
        ) + 1;

        return Math.max(meses, 0);
    }
}