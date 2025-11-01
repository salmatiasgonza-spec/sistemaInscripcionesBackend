package com.institucion.inscripciones.service;

import com.institucion.inscripciones.model.Usuario;
import com.institucion.inscripciones.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  // Registro: encripta y setea rol por defecto si falta
  public Usuario registrarUsuario(Usuario usuario) {
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    if (usuario.getRol() == null || usuario.getRol().isBlank()) {
      usuario.setRol("USER");
    }
    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> findByUsername(String username) {
    return usuarioRepository.findByUsername(username);
  }

  // requerido por Spring Security
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
  }
}