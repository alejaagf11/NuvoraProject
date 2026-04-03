package com.nuvora.backend_finanzas.dto;

import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDTO {

    private Long transaccionId;
    private Double montoTransaccion;
    private String descTransaccion;
    private LocalDate fechaTransaccion;
    private TipoTransaccion tipo;
    private Long categoriaId;
    private Long usuarioId;

    // Convierte este DTO en entidad
    public Transaccion toEntity(Categoria categoria) {
        Transaccion transaccion = new Transaccion();
        transaccion.setMontoTransaccion(this.montoTransaccion);
        transaccion.setDescTransaccion(this.descTransaccion);
        transaccion.setFechaTransaccion(this.fechaTransaccion != null ? this.fechaTransaccion : LocalDate.now());
        transaccion.setTipo(this.tipo);
        transaccion.setCategoria(categoria);
        return transaccion;
    }

    // Convierte entidad a DTO
    public static TransaccionDTO fromEntity(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setTransaccionId(transaccion.getTransaccionId());
        dto.setMontoTransaccion(transaccion.getMontoTransaccion());
        dto.setDescTransaccion(transaccion.getDescTransaccion());
        dto.setFechaTransaccion(transaccion.getFechaTransaccion());
        dto.setTipo(transaccion.getTipo());
        dto.setCategoriaId(transaccion.getCategoria().getCategoriaId());
        dto.setUsuarioId(transaccion.getUsuario().getUsuarioId());
        return dto;
    }
}