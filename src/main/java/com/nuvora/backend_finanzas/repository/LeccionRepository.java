package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface LeccionRepository extends JpaRepository<Leccion, Long> {
    List<Leccion> findByModuloAprendizajeAndActivoTrueOrderByOrdenLeccionAsc(ModuloAprendizaje moduloAprendizaje);

    Integer countByModuloAprendizajeAndActivoTrue(ModuloAprendizaje moduloAprendizaje);

    boolean existsByModuloAprendizajeAndOrdenLeccion(ModuloAprendizaje moduloAprendizaje, Integer ordenLeccion);

    List<Leccion> findByModuloAprendizajeOrderByOrdenLeccionAsc(ModuloAprendizaje moduloAprendizaje);

    Optional<Leccion> findTopByModuloAprendizajeAndActivoTrueAndOrdenLeccionLessThanOrderByOrdenLeccionDesc(
            ModuloAprendizaje moduloAprendizaje,
            Integer ordenLeccion
    );
}
