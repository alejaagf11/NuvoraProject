package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetasAhorroRepository extends JpaRepository<MetasAhorro, Long> {

    List<MetasAhorro> findByUsuario(Usuario usuario);
    Optional<MetasAhorro> findByMetaAhorroIdAndUsuario(Long metaAhorroId, Usuario usuario);
}
