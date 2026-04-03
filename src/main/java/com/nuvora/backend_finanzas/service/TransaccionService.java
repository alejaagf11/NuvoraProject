package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.TransaccionDTO;
import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransaccionService {
    TransaccionDTO createTransaccion (TransaccionDTO transaccionDTO, Usuario usuario);

    List<TransaccionDTO> listTransaccion (Usuario usuario);

    List<TransaccionDTO> filterByType(String tipo, Usuario usuario);

    List<TransaccionDTO> filterByCategoria(Long categoriaId, Usuario usuario);

     TransaccionDTO updateTransaccion( Long transaccionId, TransaccionDTO transaccionDTO, Usuario usuario);

     void deleteTransaccion (Long transaccionId, Usuario usuario);

     Double calcularSaldoUsuario(Usuario usuario);





}
