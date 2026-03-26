package com.nuvora.backend_finanzas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "ahorro_mensual")
    private Double ahorroMensual;

    @Column(name = "montoAhorrado")
    private Double montoAhorrado = 0.0;

    public Double getMontoRestante(){
        if (montoObjetivo == null || montoAhorrado == null) return 0.0;
        return montoObjetivo - montoAhorrado;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "metasAhorro", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Abono> abonos;

}
