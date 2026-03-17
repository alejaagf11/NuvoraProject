package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoUsuario(String correoUsuario);

}
