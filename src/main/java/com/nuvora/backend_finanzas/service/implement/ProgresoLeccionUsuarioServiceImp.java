package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.dto.ProgresoLeccionUsuarioDTO;
import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ProgresoLeccionUsuario;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.ProgresoLeccionUsuarioRepository;
import com.nuvora.backend_finanzas.service.LeccionService;
import com.nuvora.backend_finanzas.service.ModuloAprendizajeService;
import com.nuvora.backend_finanzas.service.ProgresoLeccionUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgresoLeccionUsuarioServiceImp implements ProgresoLeccionUsuarioService {
    @Autowired
    private ProgresoLeccionUsuarioRepository progresoLeccionUsuarioRepository;

    @Autowired
    private LeccionService leccionService;

    @Autowired
    private ModuloAprendizajeService moduloAprendizajeService;


    @Override
    public ProgresoLeccionUsuarioDTO completarLeccion(Long leccionId, Usuario usuario){
        leccionService.validarLeccionDesbloqueada(leccionId, usuario);

        Leccion leccion = leccionService.getLeccionEntityById(leccionId);

        ProgresoLeccionUsuario progreso = progresoLeccionUsuarioRepository
                .findByUsuarioAndLeccion(usuario, leccion)
                .orElse(new ProgresoLeccionUsuario());

        progreso.setUsuario(usuario);
        progreso.setLeccion(leccion);
        progreso.setCompletada(true);
        progreso.setFechaCompletada(LocalDate.now());

        ProgresoLeccionUsuario saved = progresoLeccionUsuarioRepository.save(progreso);
        return ProgresoLeccionUsuarioDTO.fromEntity(saved);
    }

    @Override
    public List<ProgresoLeccionUsuarioDTO> listProgresoUsuario(Usuario usuario){
        return progresoLeccionUsuarioRepository.findByUsuario(usuario)
                .stream()
                .map(ProgresoLeccionUsuarioDTO::fromEntity)
                .toList();
    }

    @Override
    public List<ModuloProgresoResumDTO> getProgresoModulos(Usuario usuario){
        return moduloAprendizajeService.listModuloProgreso(usuario);
    }
}
