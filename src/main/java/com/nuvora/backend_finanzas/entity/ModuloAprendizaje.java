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
public class ModuloAprendizaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modulo_id")
    private Long moduloId;

    @Column(name = "titulo_modulo", nullable = false)
    private String tituloModulo;

    @Column(name = "descripcion_modulo", nullable = false)
    private String descripcionModulo;

    @Column(name = "orden_modulo", nullable = false, unique = true)
    private Integer ordenModulo;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "moduloAprendizaje", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Leccion> lecciones;
}
