package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuario(Usuario usuario);

    Optional<Categoria> findByCategoriaIdAndUsuario(Long categoriaId, Usuario usuario);
}
