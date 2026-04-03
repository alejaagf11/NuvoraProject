package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long categoriaId;
    private String nombreCategoria;
    private TipoTransaccion tipoCategoria;
    private Long usuarioId;

    //  DTO → ENTITY
    public Categoria toEntity(Usuario usuario){
        Categoria categoria = new Categoria();

        categoria.setNombreCategoria(this.nombreCategoria);
        categoria.setTipoCategoria(this.tipoCategoria);
        categoria.setUsuario(usuario);

        return categoria;
    }

    //  ENTITY → DTO
    public static CategoriaDTO fromEntity(Categoria categoria){
        CategoriaDTO dto = new CategoriaDTO();

        dto.setCategoriaId(categoria.getCategoriaId());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        dto.setTipoCategoria(categoria.getTipoCategoria());
        dto.setUsuarioId(categoria.getUsuario().getUsuarioId());

        return dto;
    }
}