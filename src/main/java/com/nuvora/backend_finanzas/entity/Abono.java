package com.nuvora.backend_finanzas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abono_id")
    private Long abonoId;

    @Column(name = "fecha", nullable=false)
    private LocalDate fecha;

    @Column(name = "monto")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "metaAhorro_id")
    @JsonBackReference
    private MetasAhorro metasAhorro;

}
