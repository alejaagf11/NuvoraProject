package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloAprendizajeDTO {

    private Long moduloId;
    private String tituloModulo;
    private String descripcionModulo;
    private Integer ordenModulo;
    private Boolean activo;

    public ModuloAprendizaje toEntity(){
        ModuloAprendizaje modulo = new ModuloAprendizaje();
         modulo.setModuloId(this.moduloId);
         modulo.setTituloModulo(this.tituloModulo);
         modulo.setDescripcionModulo(this.descripcionModulo);
         modulo.setOrdenModulo(this.ordenModulo);
         modulo.setActivo(this.activo != null ? this.activo : true);

         return modulo;
    }

    public  static ModuloAprendizajeDTO fromEntity(ModuloAprendizaje modulo){
        ModuloAprendizajeDTO dto = new ModuloAprendizajeDTO();
        dto.setModuloId(modulo.getModuloId());
        dto.setTituloModulo(modulo.getTituloModulo());
        dto.setDescripcionModulo(modulo.getDescripcionModulo());
        dto.setOrdenModulo(modulo.getOrdenModulo());
        dto.setActivo(modulo.getActivo());

        return dto;
    }
}
