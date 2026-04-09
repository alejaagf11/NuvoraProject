package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.MetasAhorroDTO;
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
    public MetasAhorroDTO createMeta(MetasAhorroDTO metasAhorroDTO, Usuario usuario) {

        MetasAhorro meta = metasAhorroDTO.toEntity();
        meta.setUsuario(usuario);

        //creacion variables de tiempo
        LocalDate hoy = LocalDate.now();
        long meses = ChronoUnit.MONTHS.between(hoy, meta.getFechaLimite());

        if(meses <= 0){
            throw new RuntimeException("La fecha limite para la meta debe der mayor a hoy");
        }

        //Calculo de dinero a ahorrar
        double ahorroMensual = meta.getMontoObjetivo() / meses;

        //redondear numeros a 2 decimales
        ahorroMensual = Math.round(ahorroMensual * 100.0) / 100.0;

        meta.setAhorroMensual(ahorroMensual);
        meta.setMontoAhorrado(0.0);

        MetasAhorro saved = metasAhorroRepository.save(meta);
        return MetasAhorroDTO.fromEntity(saved);

    }

    @Override
    public List<MetasAhorroDTO> listMeta(Usuario usuario) {

        return metasAhorroRepository.findByUsuario(usuario)
                .stream()
                .map(MetasAhorroDTO::fromEntity)
                .toList();
    }

    @Override
    @SneakyThrows
    public MetasAhorroDTO getMetaById(Long metaAhorroId, Usuario usuario) {
        return MetasAhorroDTO.fromEntity(getMetaEntityById(metaAhorroId, usuario));
    }

    @Override
    @SneakyThrows
    public MetasAhorroDTO updateMeta(Long metaAhorroId, MetasAhorroDTO metasAhorroDTO, Usuario usuario) {
        MetasAhorro meta = getMetaEntityById(metaAhorroId, usuario);

        if (metasAhorroDTO.getNombreMeta() != null)
            meta.setNombreMeta(metasAhorroDTO.getNombreMeta());

        if (metasAhorroDTO.getMontoObjetivo() != null)
            meta.setMontoObjetivo(metasAhorroDTO.getMontoObjetivo());

        if (metasAhorroDTO.getFechaLimite() != null)
            meta.setFechaLimite(metasAhorroDTO.getFechaLimite());

        LocalDate hoy = LocalDate.now();
        long meses = ChronoUnit.MONTHS.between(hoy, meta.getFechaLimite());

        if(meses <= 0){
            throw new RuntimeException("Fecha inválida");
        }

        double montoAhorrado = meta.getMontoAhorrado() != null ? meta.getMontoAhorrado() : 0.0;
        double montoRestante = meta.getMontoObjetivo() - montoAhorrado;

        double ahorroMensual = Math.round(
                (meta.getMontoObjetivo()/ meses) * 100.0
        ) / 100.0;

        if (montoRestante <= 0) {
            ahorroMensual = 0.0;
        }

        meta.setAhorroMensual(ahorroMensual);

        MetasAhorro updated = metasAhorroRepository.save(meta);
         return MetasAhorroDTO.fromEntity(updated);


    }

    @Override
    @SneakyThrows
    public void deleteMeta(Long metaAhorroId, Usuario usuario) {
        MetasAhorro meta = getMetaEntityById(metaAhorroId, usuario);
        metasAhorroRepository.delete(meta);
    }

    @Override
    public MetasAhorroDTO abonar(Long metaAhorroId,Double monto ,Usuario usuario){
        MetasAhorro meta = getMetaEntityById(metaAhorroId, usuario);

        if (monto<= 0){
            throw new RuntimeException("El monto debe de ser mayor a 0");
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

        LocalDate hoy = LocalDate.now();
        long mesesRest = calcularMesesRestantes(hoy, meta.getFechaLimite());

        if (mesesRest <= 0){
            mesesRest = 1;
        }

        double rest = meta.getMontoObjetivo() - meta.getMontoAhorrado();

        double nuevoMensual = rest / mesesRest;
        nuevoMensual = Math.round(nuevoMensual * 100.0) / 100.0;

        if (rest <= 0){
            nuevoMensual = 0;
        }
        meta.setAhorroMensual(nuevoMensual);

        MetasAhorro saved = metasAhorroRepository.save(meta);
        return MetasAhorroDTO.fromEntity(saved);
    }

    private MetasAhorro getMetaEntityById(Long id, Usuario usuario){
        return metasAhorroRepository
                .findByMetaAhorroIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));
    }

    private long calcularMesesRestantes(LocalDate hoy, LocalDate fechaLimite) {
        if (fechaLimite == null || !fechaLimite.isAfter(hoy)) {
            return 0;
        }

        long meses = ChronoUnit.MONTHS.between(
                hoy.withDayOfMonth(1),
                fechaLimite.withDayOfMonth(1)
        ) + 1;

        return Math.max(meses, 0);
    }
}
