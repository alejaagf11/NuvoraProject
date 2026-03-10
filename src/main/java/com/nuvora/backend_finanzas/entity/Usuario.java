package com.nuvora.backend_finanzas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "usuario_nombre", nullable = false)
    private String nombreUsuario;

    @Column(name = "usuario_correo", unique = true)
    private String correoUsuario;

    @Column(name = "usuario_contrasena", nullable = false)
    private String contrasenaUsuario;

    @OneToMany(mappedBy = "usuario") // mapped referencia la relacion ya creada
    private List<Transaccion> transacciones;

    @OneToMany(mappedBy = "usuario") // mapped referencia la relacion ya creada
    private List<Presupuesto> presupuestos;

    @OneToMany(mappedBy = "usuario")
    private List<MetasAhorro> metaAhorros;


}
