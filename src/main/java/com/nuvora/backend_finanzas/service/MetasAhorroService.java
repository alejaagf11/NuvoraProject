package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MetasAhorroService {

    MetasAhorro createMeta (MetasAhorro metasAhorro, Usuario usuario);

    List<MetasAhorro> listMeta (Usuario usuario);

    MetasAhorro getMetaById (Long metaAhorroId, Usuario usuario) throws Exception;

    MetasAhorro updateMeta (Long metaAhorroId, MetasAhorro metasAhorro, Usuario usuario) throws Exception;

    void deleteMeta (Long metaAhorroId, Usuario usuario) throws Exception;

    MetasAhorro abonar(Long metaAhorroId,Double monto ,Usuario usuario);
}
