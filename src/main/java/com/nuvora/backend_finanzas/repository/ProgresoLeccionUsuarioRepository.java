package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.entity.ProgresoLeccionUsuario;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgresoLeccionUsuarioRepository extends JpaRepository<ProgresoLeccionUsuario, Long>{
    Optional<ProgresoLeccionUsuario> findByUsuarioAndLeccion(Usuario usuario, Leccion leccion);
    List<ProgresoLeccionUsuario> findByUsuario(Usuario usuario);
    Integer countByUsuarioAndLeccion_ModuloAprendizaje_ModuloIdAndCompletadaTrue(Usuario usuario, Long moduloId);
}
