package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.dto.UsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
     UsuarioDTO registerUsuario (UsuarioDTO usuarioDTO);

     UsuarioDTO loginUsuario ( String correoUsuario, String contrasenaUsuario);

     List<UsuarioDTO> listUsuario ();

     UsuarioDTO updateUsuario ( Usuario usuarioAutenticado, UsuarioDTO datosNuevos);

     void deleteUsuario (Usuario usuarioAutenticado) throws Exception;

     UsuarioDTO getUsuarioById (Long usuarioId);

    UsuarioDTO saveUsuario(Usuario usuario);

    Usuario getUsuarioByCorreo(String correoUsuario);

    Usuario getUsuarioEntityById(Long usuarioId);

}
