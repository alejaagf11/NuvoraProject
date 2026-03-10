package com.nuvora.backend_finanzas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tips {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tip_id")
    private Long tipId;

    @Column(name = "titulo_tip", nullable = false)
    private String tituloTip;

    @Column(name = "descripcion_tip", nullable = false)
    private String descripcionTip;
}
