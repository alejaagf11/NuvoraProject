package com.nuvora.backend_finanzas.service;

import com.nuvora.backend_finanzas.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
     Usuario registerUsuario (Usuario usuario);

     Usuario loginUsuario ( String correoUsuario, String contrasenaUsuario);

     List<Usuario> listUsuario ();

     Usuario updateUsuario (Long usuarioId, Usuario usuario);

     void deleteUsuario (Long usuarioId) throws Exception;

     Usuario getUsuarioById (Long usuarioId);

}
