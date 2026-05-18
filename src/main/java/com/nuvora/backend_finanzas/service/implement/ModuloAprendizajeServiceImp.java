package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.ModuloAprendizajeDTO;
import com.nuvora.backend_finanzas.dto.ModuloProgresoResumDTO;
import com.nuvora.backend_finanzas.entity.ModuloAprendizaje;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.LeccionRepository;
import com.nuvora.backend_finanzas.repository.ModuloAprendizajeRepository;
import com.nuvora.backend_finanzas.repository.ProgresoLeccionUsuarioRepository;
import com.nuvora.backend_finanzas.service.ModuloAprendizajeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuloAprendizajeServiceImp implements ModuloAprendizajeService {
    @Autowired
    private ModuloAprendizajeRepository moduloAprendizajeRepository;

    @Autowired
    private LeccionRepository leccionRepository;

    @Autowired
    private ProgresoLeccionUsuarioRepository progresoLeccionUsuarioRepository;

    @Override
    public ModuloAprendizajeDTO createModulo(ModuloAprendizajeDTO moduloDTO){

        ModuloAprendizaje modulo = moduloDTO.toEntity();

        if (modulo.getOrdenModulo() == null || modulo.getOrdenModulo() < 1){
            int ultimoOrden = moduloAprendizajeRepository.findAllByOrderByOrdenModuloAsc()
                    .stream()
                    .map(ModuloAprendizaje::getOrdenModulo)
                    .max(Integer::compareTo)
                    .orElse(0);
            modulo.setOrdenModulo(ultimoOrden + 1);

        } else if (moduloAprendizajeRepository.existsByOrdenModulo(modulo.getOrdenModulo())) {
           throw new RuntimeException("Ya existe un modulo con ese orden");
        }

        ModuloAprendizaje saved = moduloAprendizajeRepository.save(modulo);
        return ModuloAprendizajeDTO.fromEntity(saved);
    }

    @Override
    public List<ModuloAprendizajeDTO> listModulos(){
        return moduloAprendizajeRepository.findByActivoTrueOrderByOrdenModuloAsc()
                .stream()
                .map(ModuloAprendizajeDTO::fromEntity)
                .toList();
    }

    @Override
    public List<ModuloProgresoResumDTO> listModuloProgreso(Usuario usuario){
        List<ModuloAprendizaje> modulos = moduloAprendizajeRepository.findByActivoTrueOrderByOrdenModuloAsc();
        List<ModuloProgresoResumDTO> resp = new ArrayList<>();

        boolean desbloqueado = true;

        for(ModuloAprendizaje modulo: modulos){
            int totalLecciones = leccionRepository.countByModuloAprendizajeAndActivoTrue(modulo);
            int leccionCompletadas = progresoLeccionUsuarioRepository
                    .countByUsuarioAndLeccion_ModuloAprendizaje_ModuloIdAndCompletadaTrue(usuario, modulo.getModuloId());

            double porcentaje = 0.0;
            if (totalLecciones > 0){
                porcentaje = (leccionCompletadas * 100.0) / totalLecciones;
            }

            ModuloProgresoResumDTO dto = new ModuloProgresoResumDTO();
            dto.setModuloId(modulo.getModuloId());
            dto.setTituloModulo(modulo.getTituloModulo());
            dto.setDescripcionModulo(modulo.getDescripcionModulo());
            dto.setOrdenModulo(modulo.getOrdenModulo());
            dto.setTotalLecciones(totalLecciones);
            dto.setLeccionesCompletadas(leccionCompletadas);
            dto.setPorcentajeProgreso(Math.round(porcentaje * 100.0)/100.0);
            dto.setDesbloqueado(desbloqueado);

            resp.add(dto);
            desbloqueado = porcentaje >= 100.0;
        }

        return resp;
    }

    @Override
    @SneakyThrows
    public ModuloAprendizajeDTO getModuloById(Long moduloId){
       return ModuloAprendizajeDTO.fromEntity(getModuloEntityById(moduloId));
    }

    @Override
    public ModuloAprendizajeDTO updateModulo(Long moduloId, ModuloAprendizajeDTO moduloDTO){
        ModuloAprendizaje modulo = getModuloEntityById(moduloId);

        if (moduloDTO.getTituloModulo() != null){
            modulo.setTituloModulo(moduloDTO.getTituloModulo());
        }

        if (moduloDTO.getDescripcionModulo() != null){
            modulo.setDescripcionModulo(moduloDTO.getDescripcionModulo());
        }


        if (moduloDTO.getOrdenModulo() != null){
            Integer nuevoOrden = moduloDTO.getOrdenModulo();

            if (nuevoOrden < 1){
                throw new RuntimeException("El orden del modulo debe ser mayor que que 0");
            }

            boolean ordenOcupado = moduloAprendizajeRepository.findByActivoTrueOrderByOrdenModuloAsc()
                    .stream()
                    .anyMatch(m -> !m.getModuloId().equals(modulo.getModuloId())
                    && m.getOrdenModulo().equals(nuevoOrden)
                    );
            if (ordenOcupado){
                throw new RuntimeException("Ya existe otro modulo con ese orden");
            }

            modulo.setOrdenModulo(nuevoOrden);

        }

        if (moduloDTO.getActivo() != null){
            modulo.setActivo(moduloDTO.getActivo());
        }
        ModuloAprendizaje update = moduloAprendizajeRepository.save(modulo);
        return ModuloAprendizajeDTO.fromEntity(update);
    }

    @Override
    public void deleteModulo(Long moduloId){
        ModuloAprendizaje modulo = getModuloEntityById(moduloId);
        moduloAprendizajeRepository.delete(modulo);
    }

    @Override
    public ModuloAprendizaje getModuloEntityById(Long moduloId){
        return moduloAprendizajeRepository.findById(moduloId)
                .orElseThrow(()-> new RuntimeException("Modulo no encontrado"));
    }

}
