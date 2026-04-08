package com.nuvora.backend_finanzas.dto;
import lombok.Data;

@Data
public class GastoFijoDTO {
    private String nombre;
    private double monto;
    private int diaPago;
}
