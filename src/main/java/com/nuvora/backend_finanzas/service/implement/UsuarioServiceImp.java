package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.UsuarioRepository;
import com.nuvora.backend_finanzas.service.UsuarioService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService {
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImp(UsuarioRepository usuarioRepository,
                             BCryptPasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registerUsuario (Usuario usuario){

        usuario.setContrasenaUsuario(
                passwordEncoder.encode(usuario.getContrasenaUsuario())
        );
        usuario.setRolUsuario("USER");
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario loginUsuario ( String correoUsuario, String contrasenaUsuario){
        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByCorreoUsuario(correoUsuario);
        if (usuarioOptional.isPresent()) {

            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(contrasenaUsuario,
                    usuario.getContrasenaUsuario())) {

                return usuario;
            }
        }

        throw new RuntimeException("Informacion Incorrecta");
    }

    @Override
    public List<Usuario> listUsuario(){
        return usuarioRepository.findAll();
    }

    @Override
    @SneakyThrows
    public Usuario getUsuarioById(Long usuarioId){
      return usuarioRepository.findById(usuarioId)
              .orElseThrow(() -> new Exception ("Usuario no Encontrado"));
    }

    @Override
    @SneakyThrows
    public Usuario updateUsuario(Long usuarioId, Usuario usuario){
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new Exception ("Usuario con Id " + usuarioId +"no encontrado"));
        usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
        usuarioExistente.setCorreoUsuario(usuario.getCorreoUsuario());

        // actualizar si se envia una nueva contraseña

        if (usuario.getContrasenaUsuario() != null && !usuario.getContrasenaUsuario().isEmpty()){
            usuarioExistente.setContrasenaUsuario(
                    passwordEncoder.encode(usuario.getContrasenaUsuario())
            );
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @SneakyThrows
    public void deleteUsuario( Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new Exception("Usuario con el Id" + usuarioId + "no encontrado"));

        usuarioRepository.delete(usuario);
    }
}
