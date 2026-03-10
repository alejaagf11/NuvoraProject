package com.nuvora.backend_finanzas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presupuesto_id")
    private Long presupuestoId;

    @Column(name = "limiteMontoPresu", nullable = false)
    private Double limiteMontoPresu;

    @Column (name = "mesPresupuesto", nullable = false)
    private String presupuestoMes;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
