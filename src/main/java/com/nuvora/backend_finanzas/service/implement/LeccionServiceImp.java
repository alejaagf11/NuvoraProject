package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.LeccionDTO;
import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.repository.LeccionRepository;
import com.nuvora.backend_finanzas.service.LeccionService;
import com.nuvora.backend_finanzas.service.ModuloAprendizajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeccionServiceImp implements LeccionService {

    @Autowired
    private LeccionRepository leccionRepository;

    @Autowired
    private ModuloAprendizajeService moduloAprendizajeService;

    @Override
    public LeccionDTO createLeccion(LeccionDTO leccionDTO){
        ModuloAprendizaje modulo = moduloAprendizajeService.getModuloEntityById(leccionDTO.getModuloId());
        Leccion leccion = leccionDTO.toEntity(modulo);
        Leccion saved = leccionRepository.save(leccion);
        return LeccionDTO.fromEntity(saved);
    }

    @Override
    public List<LeccionDTO> listLeccionByModulo(Long moduloId){
        ModuloAprendizaje modulo = moduloAprendizajeService.getModuloEntityById(moduloId);

        return leccionRepository.findByModuloAprendizajeAndActivoTrueOrderByOrdenLeccionAsc(modulo)
                .stream()
                .map(LeccionDTO::fromEntity)
                .toList();
    }

    @Override
    public LeccionDTO getLeccionById(Long leccionId){
        return LeccionDTO.fromEntity(getLeccionEntityById(leccionId));
    }

    @Override
    public LeccionDTO updateLeccion(Long leccionId, LeccionDTO leccionDTO){
        Leccion leccion = getLeccionEntityById(leccionId);

        if(leccionDTO.getTituloLeccion() != null){
            leccion.setTituloLeccion(leccionDTO.getTituloLeccion());
        }

        if (leccionDTO.getContenidoLeccion() != null){
            leccion.setContenidoLeccion(leccionDTO.getContenidoLeccion());
        }

        if (leccionDTO.getOrdenLeccion() != null){
            leccion.setOrdenLeccion(leccionDTO.getOrdenLeccion());
        }

        if (leccionDTO.getActivo() != null){
            leccion.setActivo(leccionDTO.getActivo());
        }

        if (leccionDTO.getModuloId() != null) {
            ModuloAprendizaje modulo = moduloAprendizajeService.getModuloEntityById(leccionDTO.getModuloId());
            leccion.setModuloAprendizaje(modulo);
        }

        Leccion updated = leccionRepository.save(leccion);
        return LeccionDTO.fromEntity(updated);
    }

    @Override
    public void deleteLeccion(Long leccionId){
        Leccion leccion = getLeccionEntityById(leccionId);
        leccionRepository.delete(leccion);
    }

    @Override
    public Leccion getLeccionEntityById(Long leccionId){
        return leccionRepository.findById(leccionId)
                .orElseThrow(()-> new RuntimeException("Leccion no encontrada"));
    }
}
