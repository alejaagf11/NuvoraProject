package com.nuvora.backend_finanzas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetasAhorro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metaAhorro_id")
    private Long metaAhorroId;

    @Column(name = "nombre_Meta", nullable=false)
    private String nombreMeta;

    @Column(name = "montoObjetivo", nullable=false)
    private Double montoObjetivo;

    @Column(name = "fechaLimite", nullable=false)
    private LocalDate fechaLimite;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
