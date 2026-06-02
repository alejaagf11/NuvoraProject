package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.UsuarioDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.repository.UsuarioRepository;
import com.nuvora.backend_finanzas.service.UsuarioService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public UsuarioDTO registerUsuario (UsuarioDTO usuarioDTO){

        Usuario usuario = usuarioDTO.toEntity();

        usuario.setContrasenaUsuario(
                passwordEncoder.encode(usuarioDTO.getContrasenaUsuario())
        );
        usuario.setRolUsuario("USER");

       Usuario saved = usuarioRepository.save(usuario);
       return UsuarioDTO.fromEntity(saved);
    }

    @Override
    public UsuarioDTO loginUsuario ( String correoUsuario, String contrasenaUsuario){
        if (correoUsuario == null || contrasenaUsuario == null) {
            throw new RuntimeException("Correo y contraseña son obligatorios");
        }

        String correoNormalizado = correoUsuario.trim().toLowerCase();
        String contrasenaLimpia = contrasenaUsuario.trim();

        Usuario usuario = usuarioRepository.findByCorreoUsuario(correoNormalizado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasenaLimpia, usuario.getContrasenaUsuario())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return UsuarioDTO.fromEntity(usuario);
    }

    @Override
    public List<UsuarioDTO> listUsuario(){
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public UsuarioDTO getUsuarioById(Long usuarioId){
      Usuario usuario = usuarioRepository.findById(usuarioId)
              .orElseThrow(() -> new Exception ("Usuario no Encontrado"));

      return UsuarioDTO.fromEntity(usuario);
    }

    @Override
    public UsuarioDTO updateUsuario(Usuario usuarioAutenticado, UsuarioDTO datosNuevos){

        Usuario usuarioBD = usuarioRepository.findById(usuarioAutenticado.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (datosNuevos.getNombreUsuario() != null && !datosNuevos.getNombreUsuario().isEmpty()) {
            usuarioBD.setNombreUsuario(datosNuevos.getNombreUsuario());
        }

        if (datosNuevos.getCorreoUsuario() != null && !datosNuevos.getCorreoUsuario().isEmpty()) {
            usuarioBD.setCorreoUsuario(datosNuevos.getCorreoUsuario());
        }

        if (datosNuevos.getMontoMensual() != null) {
            usuarioBD.setMontoMensual(datosNuevos.getMontoMensual());
        }

        if (datosNuevos.getFotoPerfil() != null && !datosNuevos.getFotoPerfil().isBlank()) {
            usuarioBD.setFotoPerfil(datosNuevos.getFotoPerfil());
        }


        if (datosNuevos.getContrasenaUsuario() != null && !datosNuevos.getContrasenaUsuario().isEmpty()) {
            usuarioBD.setContrasenaUsuario(passwordEncoder.encode(datosNuevos.getContrasenaUsuario()));
        }

        Usuario update = usuarioRepository.save(usuarioBD);

        return UsuarioDTO.fromEntity(update);
    }

    @Override
    @SneakyThrows
    public void deleteUsuario(Usuario usuarioAutenticado) {
        usuarioRepository.delete(usuarioAutenticado);
    }

    @Override
    public UsuarioDTO saveUsuario(Usuario usuario) {
        return UsuarioDTO.fromEntity(usuarioRepository.save(usuario));
    }

    @Override
    public Usuario getUsuarioByCorreo(String correoUsuario) {
        return usuarioRepository.findByCorreoUsuario(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario getUsuarioEntityById(Long usuarioId){
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UsuarioDTO updateUsuarioById(Long usuarioId, UsuarioDTO datosNuevos){

        Usuario usuarioBD = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        if (datosNuevos.getNombreUsuario() != null && !datosNuevos.getNombreUsuario().isEmpty()) {
            usuarioBD.setNombreUsuario(datosNuevos.getNombreUsuario());
        }

        if (datosNuevos.getCorreoUsuario() != null && !datosNuevos.getCorreoUsuario().isEmpty()) {
            usuarioBD.setCorreoUsuario(datosNuevos.getCorreoUsuario());
        }

        if (datosNuevos.getMontoMensual() != null) {
            usuarioBD.setMontoMensual(datosNuevos.getMontoMensual());
        }

        if (datosNuevos.getFotoPerfil() != null && !datosNuevos.getFotoPerfil().isBlank()) {
            usuarioBD.setFotoPerfil(datosNuevos.getFotoPerfil());
        }

        if (datosNuevos.getContrasenaUsuario() != null && !datosNuevos.getContrasenaUsuario().isEmpty()) {
            usuarioBD.setContrasenaUsuario(passwordEncoder.encode(datosNuevos.getContrasenaUsuario()));
        }

        if ((datosNuevos.getRolUsuario() != null && !datosNuevos.getRolUsuario().isEmpty())){
            usuarioBD.setRolUsuario(datosNuevos.getRolUsuario().toUpperCase());
        }

        Usuario update = usuarioRepository.save(usuarioBD);

        return UsuarioDTO.fromEntity(update);
    }

    @Override
    public void deleteUsuarioById(Long usuarioId){
        Usuario usuario= usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);

    }

}
