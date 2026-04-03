package com.nuvora.backend_finanzas.repository;

import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByUsuario(Usuario usuario);

    List<Transaccion> findByUsuarioAndTipo(Usuario usuario, TipoTransaccion tipo);

    List<Transaccion> findByUsuarioAndCategoria(Usuario usuario, Categoria  categoria);
}
