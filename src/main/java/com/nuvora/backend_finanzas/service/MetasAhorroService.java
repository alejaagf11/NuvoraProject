package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.entity.MetasAhorro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MetasAhorroService {

    MetasAhorro createMeta (MetasAhorro metasAhorro);

    List<MetasAhorro> listMeta ();

    MetasAhorro getMetaById (Long metaAhorroId);

    MetasAhorro updateMeta (Long metaAhorroId, MetasAhorro metasAhorro);

    void deleteMeta (Long metaAhorroId) throws Exception;
}
