package com.nuvora.backend_finanzas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leccion_id")
    private Long leccionId;

    @Column(name = "titulo_leccion", nullable = false)
    private String tituloLeccion;

    @Column(name = "contenido_leccion", nullable = false, columnDefinition = "TEXT")
    private String contenidoLeccion;

    @Column(name = "orden_leccion", nullable = false)
    private Integer ordenLeccion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private ModuloAprendizaje moduloAprendizaje;


}
