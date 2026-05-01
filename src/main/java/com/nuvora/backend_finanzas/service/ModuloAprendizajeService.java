package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.ModuloAprendizajeDTO;
import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ModuloAprendizajeService {
    ModuloAprendizajeDTO createModulo(ModuloAprendizajeDTO moduloDTO);

    List<ModuloAprendizajeDTO> listModulos();

    List<ModuloProgresoResumDTO> listModuloProgreso(Usuario usuario);

    ModuloAprendizajeDTO getModuloById(Long moduloId);

    ModuloAprendizajeDTO updateModulo(Long moduloId, ModuloAprendizajeDTO moduloDTO);

    void deleteModulo(Long moduloId);

    ModuloAprendizaje getModuloEntityById(Long ModuloId);
}
