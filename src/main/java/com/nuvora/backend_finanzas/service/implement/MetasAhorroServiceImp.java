package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.entity.Abono;
import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.AbonoRepository;
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

    @Autowired
    private AbonoRepository abonoRepository;

    @Override
    public MetasAhorro createMeta(MetasAhorro metasAhorro, Usuario usuario) {

        metasAhorro.setUsuario(usuario);

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
        metasAhorro.setMontoAhorrado(0.0);
        return metasAhorroRepository.save(metasAhorro);

    }

    @Override
    public List<MetasAhorro> listMeta(Usuario usuario) {
        return metasAhorroRepository.findByUsuario(usuario);
    }

    @Override
    @SneakyThrows
    public MetasAhorro getMetaById(Long metaAhorroId, Usuario usuario) {
        return metasAhorroRepository.findByMetaAhorroIdAndUsuario(metaAhorroId, usuario)
        .orElseThrow(() -> new Exception("Meta no Encontrada"));
    }

    @Override
    @SneakyThrows
    public MetasAhorro updateMeta(Long metaAhorroId, MetasAhorro metasAhorro, Usuario usuario) {
        MetasAhorro metasAhorroExistente = getMetaById(metaAhorroId, usuario);

        metasAhorroExistente.setNombreMeta(metasAhorro.getNombreMeta());
        metasAhorroExistente.setMontoObjetivo(metasAhorro.getMontoObjetivo());
        metasAhorroExistente.setFechaLimite(metasAhorro.getFechaLimite());

        LocalDate hoy = LocalDate.now();
        long meses = ChronoUnit.MONTHS.between(hoy, metasAhorroExistente.getFechaLimite());
        double ahorroMensual = Math.round((metasAhorroExistente.getMontoObjetivo()/ meses) * 100.0) / 100.0;

        metasAhorroExistente.setAhorroMensual(ahorroMensual);
        return metasAhorroRepository.save(metasAhorroExistente);
    }

    @Override
    @SneakyThrows
    public void deleteMeta(Long metaAhorroId, Usuario usuario) {
        MetasAhorro metasAhorroExistente = getMetaById(metaAhorroId, usuario);
        metasAhorroRepository.delete(metasAhorroExistente);
    }

    @Override
    public MetasAhorro abonar(Long metaAhorroId,Double monto ,Usuario usuario){
        MetasAhorro meta = getMetaById(metaAhorroId, usuario);

        if (monto<= 0){
            throw new RuntimeException("El monto debe de ser mayor a 0");
        }

        if (meta.getMontoAhorrado() == null) {
            meta.setMontoAhorrado(0.0);
        }

        double newMonto = meta.getMontoAhorrado() + monto;

        if (newMonto > meta.getMontoObjetivo()){
            throw new RuntimeException("No se puede exceder al monto objetivo");
        }

        Abono abono = new Abono();
        abono.setMonto(monto);
        abono.setFecha(LocalDate.now());
        abono.setMetasAhorro(meta);

        abonoRepository.save(abono);

        meta.setMontoAhorrado(newMonto);
        return metasAhorroRepository.save(meta);
    }
}
