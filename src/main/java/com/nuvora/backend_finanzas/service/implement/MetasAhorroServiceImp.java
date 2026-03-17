package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.MetasAhorroRepository;
import com.nuvora.backend_finanzas.service.MetasAhorroService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MetasAhorroServiceImp implements MetasAhorroService {

    @Autowired
    private MetasAhorroRepository metasAhorroRepository;

    @Override
    public MetasAhorro createMeta(MetasAhorro metasAhorro) {

        //creacion variables de tiempo
        LocalDate hoy = LocalDate.now();
        long meses = ChronoUnit.MONTHS.between(hoy, metasAhorro.getFechaLimite());

        if(meses <= 0){
            throw new RuntimeException("La fecha limite para la meta debe der mayor a hoy");
        }

        //Calculo de dinero a ahorrar
        double ahorroMensual = metasAhorro.getMontoObjetivo() / meses;

        //redondear numeros a 2 decimales
        ahorroMensual = Math.round(ahorroMensual * 100.0) / 100.0;

        metasAhorro.setAhorroMensual(ahorroMensual);
        return metasAhorroRepository.save(metasAhorro);

    }

    @Override
    public List<MetasAhorro> listMeta() {
        return metasAhorroRepository.findAll();
    }

    @Override
    @SneakyThrows
    public MetasAhorro getMetaById(Long metaAhorroId) {
        return metasAhorroRepository.findById(metaAhorroId)
        .orElseThrow(() -> new Exception("Meta no Encontrada"));
    }

    @Override
    @SneakyThrows
    public MetasAhorro updateMeta(Long metaAhorroId, MetasAhorro metasAhorro) {
        MetasAhorro metasAhorroExistente = metasAhorroRepository.findById(metaAhorroId)
                .orElseThrow(() -> new Exception("Meta con Id " + metaAhorroId + "no encontrado"));
        metasAhorroExistente.setNombreMeta(metasAhorro.getNombreMeta());
        metasAhorroExistente.setMontoObjetivo(metasAhorro.getMontoObjetivo());
        metasAhorroExistente.setFechaLimite(metasAhorro.getFechaLimite());

        return metasAhorroRepository.save(metasAhorroExistente);
    }

    @Override
    @SneakyThrows
    public void deleteMeta(Long metaAhorroId) {
        MetasAhorro metasAhorro = metasAhorroRepository.findById(metaAhorroId)
                .orElseThrow(() -> new Exception("Meta con el  Id" + metaAhorroId + "no encontrado"));
        metasAhorroRepository.delete(metasAhorro);
    }
}
