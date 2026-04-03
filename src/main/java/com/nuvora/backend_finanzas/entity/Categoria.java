package com.nuvora.backend_finanzas.entity;

import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Long categoriaId;

    @Column(name = "nombre_categoria", nullable=false)
    private String nombreCategoria;

    @Column(name = "tipo_categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoCategoria; // Ingreso o Gasto

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "categoria")
    private List<Transaccion> transacciones;

    @OneToMany(mappedBy = "categoria")
    private List<Presupuesto> presupuestos;
}
