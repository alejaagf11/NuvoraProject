package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.CategoriaDTO;
import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Usuario;

import java.util.List;

public interface CategoriaService {
    CategoriaDTO createCategoria(CategoriaDTO categoriaDTO, Usuario usuario);

    List<CategoriaDTO> listCategoria(Usuario usuario);

    CategoriaDTO getCategoriaById(Long categoriaId, Usuario usuario) throws Exception;

    CategoriaDTO updateCategoria(Long categoriaId, CategoriaDTO categoriaDTO, Usuario usuario) throws Exception;

    void deleteCategoria(Long categoriaId, Usuario usuario) throws Exception;
}
