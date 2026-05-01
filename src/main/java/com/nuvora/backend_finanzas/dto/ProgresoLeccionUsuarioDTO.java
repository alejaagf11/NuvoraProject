package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ProgresoLeccionUsuario;
import com.nuvora.backend_finanzas.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoLeccionUsuarioDTO {

    private Long progresoId;
    private Long usuarioId;
    private Long leccionId;
    private Boolean completada;
    private LocalDate fechaCompletada;

    public ProgresoLeccionUsuario toEntity(Usuario usuario, Leccion leccion) {
        ProgresoLeccionUsuario progreso = new ProgresoLeccionUsuario();
        progreso.setProgresoId(this.progresoId);
        progreso.setUsuario(usuario);
        progreso.setLeccion(leccion);
        progreso.setCompletada(this.completada != null ? this.completada : false);
        progreso.setFechaCompletada(this.fechaCompletada);
        return progreso;
    }

    public static ProgresoLeccionUsuarioDTO fromEntity(ProgresoLeccionUsuario progreso) {
        ProgresoLeccionUsuarioDTO dto = new ProgresoLeccionUsuarioDTO();
        dto.setProgresoId(progreso.getProgresoId());
        dto.setUsuarioId(progreso.getUsuario().getUsuarioId());
        dto.setLeccionId(progreso.getLeccion().getLeccionId());
        dto.setCompletada(progreso.getCompletada());
        dto.setFechaCompletada(progreso.getFechaCompletada());
        return dto;
    }
}
