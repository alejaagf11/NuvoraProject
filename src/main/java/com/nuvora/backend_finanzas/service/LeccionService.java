package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.LeccionDTO;
import com.nuvora.backend_finanzas.entity.Leccion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeccionService {

    LeccionDTO createLeccion (LeccionDTO leccionDTO);

    List<LeccionDTO> listLeccionByModulo(Long moduloId);

    LeccionDTO getLeccionById(Long leccionId);

    LeccionDTO updateLeccion(Long leccionId, LeccionDTO leccionDTO);

    void deleteLeccion(Long leccionId);

    Leccion getLeccionEntityById(Long leccionId);

}
