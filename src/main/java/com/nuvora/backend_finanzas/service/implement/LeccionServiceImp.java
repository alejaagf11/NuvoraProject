package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.LeccionDTO;
import com.nuvora.backend_finanzas.entity.Leccion;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.repository.LeccionRepository;
import com.nuvora.backend_finanzas.repository.ProgresoLeccionUsuarioRepository;
import com.nuvora.backend_finanzas.service.LeccionService;
import com.nuvora.backend_finanzas.service.ModuloAprendizajeService;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.ProgresoLeccionUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeccionServiceImp implements LeccionService {

    @Autowired
    private LeccionRepository leccionRepository;

    @Autowired
    private ModuloAprendizajeService moduloAprendizajeService;

    @Autowired
    private ProgresoLeccionUsuarioRepository progresoLeccionUsuarioRepository;

    @Override
    public LeccionDTO createLeccion(LeccionDTO leccionDTO){
        ModuloAprendizaje modulo = moduloAprendizajeService.getModuloEntityById(leccionDTO.getModuloId());
        Leccion leccion = leccionDTO.toEntity(modulo);

        if (leccion.getOrdenLeccion() == null || leccion.getOrdenLeccion() <1){
            int ultimoOrden = leccionRepository.findByModuloAprendizajeOrderByOrdenLeccionAsc(modulo)
                    .stream()
                    .map(Leccion::getOrdenLeccion)
                    .max(Integer::compareTo)
                    .orElse(0);
            leccion.setOrdenLeccion(ultimoOrden + 1);
        } else if (leccionRepository.existsByModuloAprendizajeAndOrdenLeccion(modulo, leccion.getOrdenLeccion())) {
            throw new RuntimeException("Ya existe una leccion con este modulo");
        }

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

        ModuloAprendizaje moduloDestino = leccion.getModuloAprendizaje();
        if (leccionDTO.getModuloId() != null){
            moduloDestino = moduloAprendizajeService.getModuloEntityById(leccionDTO.getModuloId());
        }

        Integer nuevoOrden = leccionDTO.getOrdenLeccion() != null
                ? leccionDTO.getOrdenLeccion()
                : leccion.getOrdenLeccion();

        if(nuevoOrden == null || nuevoOrden < 1){
            throw new RuntimeException("El orden de la leccion debe ser mayor que 0");
        }

        boolean ordenOcupado = leccionRepository.findByModuloAprendizajeOrderByOrdenLeccionAsc(moduloDestino)
                .stream()
                .anyMatch(l -> !l.getLeccionId().equals(leccion.getLeccionId())
                && l.getOrdenLeccion().equals(nuevoOrden));

        if (ordenOcupado){
            throw new RuntimeException("Ya existe otra leccion con ese orden en este modulo");
        }

        leccion.setModuloAprendizaje(moduloDestino);
        leccion.setOrdenLeccion(nuevoOrden);

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

    @Override
    public LeccionDTO getLeccionByIdParaUsuario(Long leccionId, Usuario usuario) {
        validarLeccionDesbloqueada(leccionId, usuario);
        return LeccionDTO.fromEntity(getLeccionEntityById(leccionId));
    }

    @Override
    public void validarLeccionDesbloqueada(Long leccionId, Usuario usuario) {
        Leccion leccion = getLeccionEntityById(leccionId);

        if (Boolean.FALSE.equals(leccion.getActivo())) {
            throw new RuntimeException("La lección no está disponible");
        }

        if ("ADMIN".equalsIgnoreCase(usuario.getRolUsuario())) {
            return;
        }

        Leccion leccionAnterior = leccionRepository
                .findTopByModuloAprendizajeAndActivoTrueAndOrdenLeccionLessThanOrderByOrdenLeccionDesc(
                        leccion.getModuloAprendizaje(),
                        leccion.getOrdenLeccion()
                )
                .orElse(null);

        if (leccionAnterior == null) {
            return;
        }

        boolean anteriorCompletada = progresoLeccionUsuarioRepository
                .existsByUsuarioAndLeccionAndCompletadaTrue(usuario, leccionAnterior);

        if (!anteriorCompletada) {
            throw new RuntimeException("Debes completar la lección anterior antes de continuar");
        }
    }
}
