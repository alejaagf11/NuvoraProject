package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.CategoriaDTO;
import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.CategoriaRepository;
import com.nuvora.backend_finanzas.service.CategoriaService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImp implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO, Usuario usuario){
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(categoriaDTO.getNombreCategoria());
        categoria.setTipoCategoria(categoriaDTO.getTipoCategoria());
        categoria.setUsuario(usuario);

        Categoria saved = categoriaRepository.save(categoria);
        return categoriaDTO.fromEntity(saved);
    }

    @Override
    public List<CategoriaDTO> listCategoria(Usuario usuario){
        return categoriaRepository.findByUsuario(usuario)
                .stream()
                .map(CategoriaDTO::fromEntity)
                .toList();
    }

    @Override
    @SneakyThrows
    public CategoriaDTO updateCategoria(Long categoriaId, CategoriaDTO categoriaDTO, Usuario usuario){

        Categoria categoria = categoriaRepository
                .findByCategoriaIdAndUsuario(categoriaId, usuario)
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada"));

        if (categoriaDTO.getNombreCategoria() != null && !categoriaDTO.getNombreCategoria().isEmpty()) {
            categoria.setNombreCategoria(categoriaDTO.getNombreCategoria());
        }

        if (categoriaDTO.getTipoCategoria() != null &&
                !categoriaDTO.getTipoCategoria().equals(categoria.getTipoCategoria())) {

            if (!categoria.getTransacciones().isEmpty()) {
                throw new RuntimeException("No se puede cambiar el tipo de una categoría con transacciones");
            }

            categoria.setTipoCategoria(categoriaDTO.getTipoCategoria());
        }

        Categoria update = categoriaRepository.save(categoria);
        return CategoriaDTO.fromEntity(update);
    }

    @Override
    @SneakyThrows
    public void deleteCategoria(Long categoriaId, Usuario usuario){

        Categoria categoria = categoriaRepository
                .findByCategoriaIdAndUsuario(categoriaId, usuario)
                .orElseThrow(()-> new RuntimeException(" Categoria no encontrada"));

        if (!categoria.getTransacciones().isEmpty()) {
            throw new RuntimeException("No se puede eliminar una categoría con transacciones");
        }
        categoriaRepository.delete(categoria);

    }

    @Override
    public CategoriaDTO getCategoriaById(Long categoriaId, Usuario usuario){
        Categoria categoria = categoriaRepository
                .findByCategoriaIdAndUsuario(categoriaId, usuario)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        return CategoriaDTO.fromEntity(categoria);
    }
}
