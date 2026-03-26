package com.nuvora.backend_finanzas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

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

    @Column(name = "usuario_rol", nullable = false)
    private String rolUsuario;

    @Column(name = "usuario_nombre", nullable = false)
    private String nombreUsuario;

    @Column(name = "usuario_correo", unique = true)
    @Email(message = "Correo Invalido")
    @NotBlank(message = "Correo Campo Obligatorio")
    private String correoUsuario;

    @Column(name = "usuario_contrasena", nullable = false)
    private String contrasenaUsuario;

    @OneToMany(mappedBy = "usuario")// mapped referencia la relacion ya creada
    @JsonIgnore
    private List<Transaccion> transacciones;

    @OneToMany(mappedBy = "usuario")// mapped referencia la relacion ya creada
    @JsonIgnore
    private List<Presupuesto> presupuestos;

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<MetasAhorro> metaAhorros;

}
