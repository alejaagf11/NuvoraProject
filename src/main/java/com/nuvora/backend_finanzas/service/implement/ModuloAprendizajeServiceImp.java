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

        boolean desbloquedo = true;

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
            dto.setPorcentajeProgreso(Math.round(porcentaje * 100.0)/100.0);
            dto.setDesbloqueado(desbloquedo);

            resp.add(dto);
            desbloquedo = porcentaje >= 100.0;
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
            modulo.setOrdenModulo(moduloDTO.getOrdenModulo());
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
