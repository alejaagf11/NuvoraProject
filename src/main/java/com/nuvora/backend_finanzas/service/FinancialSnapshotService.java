package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.FinancialSnapshotDTO;
import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import com.nuvora.backend_finanzas.repository.MetasAhorroRepository;
import com.nuvora.backend_finanzas.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialSnapshotService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private MetasAhorroRepository metasAhorroRepository;

    public FinancialSnapshotDTO buildSnapshot(Usuario usuario) {
        List<Transaccion> transacciones = transaccionRepository.findByUsuario(usuario);
        List<MetasAhorro> metas = metasAhorroRepository.findByUsuario(usuario);

        double totalIngresos = transacciones.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.INGRESO)
                .mapToDouble(Transaccion::getMontoTransaccion)
                .sum();

        double totalGastos = transacciones.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.GASTO)
                .mapToDouble(Transaccion::getMontoTransaccion)
                .sum();

        Map<String, Double> gastosPorCategoria = new HashMap<>();
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo() == TipoTransaccion.GASTO && transaccion.getCategoria() != null) {
                String nombreCategoria = transaccion.getCategoria().getNombreCategoria();
                gastosPorCategoria.put(
                        nombreCategoria,
                        gastosPorCategoria.getOrDefault(nombreCategoria, 0.0) + transaccion.getMontoTransaccion()
                );
            }
        }

        double totalAhorradoMetas = metas.stream()
                .mapToDouble(meta -> meta.getMontoAhorrado() != null ? meta.getMontoAhorrado() : 0.0)
                .sum();

        double totalObjetivoMetas = metas.stream()
                .mapToDouble(meta -> meta.getMontoObjetivo() != null ? meta.getMontoObjetivo() : 0.0)
                .sum();

        double montoMensualUsuario = usuario.getMontoMensual() != null ? usuario.getMontoMensual() : 0.0;
        double saldoEstimado = montoMensualUsuario + totalIngresos - totalGastos;

        String resumenGeneral = construirResumen(usuario, montoMensualUsuario, totalIngresos, totalGastos,
                totalAhorradoMetas, totalObjetivoMetas, gastosPorCategoria);

        FinancialSnapshotDTO snapshot = new FinancialSnapshotDTO();
        snapshot.setUsuarioId(usuario.getUsuarioId());
        snapshot.setMontoMensualUsuario(montoMensualUsuario);
        snapshot.setTotalIngresos(totalIngresos);
        snapshot.setTotalGastos(totalGastos);
        snapshot.setTotalDeudasAhorro(0.0);
        snapshot.setTotalAhorradoMetas(totalAhorradoMetas);
        snapshot.setTotalObjetivoMetas(totalObjetivoMetas);
        snapshot.setSaldoEstimado(saldoEstimado);
        snapshot.setGastosPorCategoria(gastosPorCategoria);
        snapshot.setResumenGeneral(resumenGeneral);

        return snapshot;
    }

    private String construirResumen(Usuario usuario,
                                    double montoMensualUsuario,
                                    double totalIngresos,
                                    double totalGastos,
                                    double totalAhorradoMetas,
                                    double totalObjetivoMetas,
                                    Map<String, Double> gastosPorCategoria) {

        String categoriaMayorGasto = "sin categoria dominante";
        double mayorGasto = 0.0;

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            if (entry.getValue() > mayorGasto) {
                mayorGasto = entry.getValue();
                categoriaMayorGasto = entry.getKey();
            }
        }

        return "Usuario con ingreso mensual base de " + montoMensualUsuario
                + ", ingresos adicionales de " + totalIngresos
                + ", gastos acumulados de " + totalGastos
                + ", ahorro actual en metas de " + totalAhorradoMetas
                + " sobre un objetivo total de " + totalObjetivoMetas
                + ". La categoria con mayor gasto es " + categoriaMayorGasto + ".";
    }
}

