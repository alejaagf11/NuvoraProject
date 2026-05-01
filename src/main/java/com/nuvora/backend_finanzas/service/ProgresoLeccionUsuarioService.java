package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.ModuloAprendizajeDTO;
import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.dto.ProgresoLeccionUsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgresoLeccionUsuarioService {
    ProgresoLeccionUsuarioDTO completarLeccion(Long leccionId, Usuario usuario);

    List<ProgresoLeccionUsuarioDTO> listProgresoUsuario(Usuario usuario);

    List<ModuloProgresoResumDTO> getProgresoModulos(Usuario usuario);
}
