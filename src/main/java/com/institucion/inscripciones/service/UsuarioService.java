package com.institucion.inscripciones.service;

import com.institucion.inscripciones.model.Usuario;
import com.institucion.inscripciones.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectado desde la config de seguridad

    // REGISTRAR UN NUEVO USUARIO
    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptar la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        // Asegurarse de que el rol por defecto sea ADMIN (o el deseado)
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ADMIN");
        }
        return usuarioRepository.save(usuario);
    }

    // ENCONTRAR POR USERNAME (Usado para el login)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // (Otras operaciones CRUD si son necesarias)
}
