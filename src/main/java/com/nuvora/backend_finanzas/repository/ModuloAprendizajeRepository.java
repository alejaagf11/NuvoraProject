package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.entity.ProgresoLeccionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuloAprendizajeRepository extends JpaRepository<ModuloAprendizaje, Long> {
    List<ModuloAprendizaje> findByActivoTrueOrderByOrdenModuloAsc();
    boolean existsByOrdenModulo(Integer ordenModulo);
    List<ModuloAprendizaje> findAllByOrderByOrdenModuloAsc();
}
