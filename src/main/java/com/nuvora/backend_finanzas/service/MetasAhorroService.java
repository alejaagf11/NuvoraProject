package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.MetasAhorroDTO;
import com.nuvora.backend_finanzas.entity.MetasAhorro;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MetasAhorroService {

    MetasAhorroDTO createMeta (MetasAhorroDTO metasAhorroDTO, Usuario usuario);

    List<MetasAhorroDTO> listMeta (Usuario usuario);

    MetasAhorroDTO getMetaById (Long metaAhorroId, Usuario usuario) throws Exception;

    MetasAhorroDTO updateMeta (Long metaAhorroId, MetasAhorroDTO metasAhorroDTO, Usuario usuario) throws Exception;

    void deleteMeta (Long metaAhorroId, Usuario usuario) throws Exception;

    MetasAhorroDTO abonar(Long metaAhorroId,Double monto ,Usuario usuario);
}
