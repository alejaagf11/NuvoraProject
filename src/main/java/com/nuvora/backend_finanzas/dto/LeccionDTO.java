package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeccionDTO {

    private Long leccionId;
    private String tituloLeccion;
    private String contenidoLeccion;
    private Integer ordenLeccion;
    private Boolean activo;
    private Long moduloId;

    public Leccion toEntity(ModuloAprendizaje modulo){
        Leccion leccion = new Leccion();
        leccion.setLeccionId(this.leccionId);
        leccion.setTituloLeccion(this.tituloLeccion);
        leccion.setContenidoLeccion(this.contenidoLeccion);
        leccion.setOrdenLeccion(this.ordenLeccion);
        leccion.setActivo(this.activo !=null ? this.activo : true);
        leccion.setModuloAprendizaje(modulo);

        return leccion;
    }

    public static LeccionDTO fromEntity(Leccion leccion){
        LeccionDTO dto = new LeccionDTO();
        dto.setLeccionId(leccion.getLeccionId());
        dto.setTituloLeccion(leccion.getTituloLeccion());
        dto.setContenidoLeccion(leccion.getContenidoLeccion());
        dto.setOrdenLeccion(leccion.getOrdenLeccion());
        dto.setActivo(leccion.getActivo());
        dto.setModuloId(leccion.getModuloAprendizaje().getModuloId());

        return dto;
    }
}
