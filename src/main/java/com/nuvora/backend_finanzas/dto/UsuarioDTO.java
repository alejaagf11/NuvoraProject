package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long usuarioId;
    private String rolUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private String contrasenaUsuario; // solo usar en create/update
    private Double montoMensual;

    // Convierte DTO a entidad
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(this.usuarioId);
        usuario.setRolUsuario(this.rolUsuario);
        usuario.setNombreUsuario(this.nombreUsuario);
        usuario.setCorreoUsuario(this.correoUsuario);
        if (this.contrasenaUsuario != null) {
            usuario.setContrasenaUsuario(this.contrasenaUsuario);
        }
        usuario.setMontoMensual(this.montoMensual != null ? this.montoMensual : 0);
        return usuario;
    }

    // Convierte entidad a DTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setRolUsuario(usuario.getRolUsuario());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setCorreoUsuario(usuario.getCorreoUsuario());
        // NO enviar contraseña al front
        dto.setMontoMensual(usuario.getMontoMensual());
        return dto;
    }




}
