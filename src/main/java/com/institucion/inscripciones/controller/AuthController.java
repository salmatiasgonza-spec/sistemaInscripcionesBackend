package com.institucion.inscripciones.controller;

import com.institucion.inscripciones.dto.LoginRequest;
import com.institucion.inscripciones.dto.RegistroRequest;
import com.institucion.inscripciones.model.Usuario;
import com.institucion.inscripciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // REGISTRAR USUARIO
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest registroRequest) {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(registroRequest.getUsername());
            nuevoUsuario.setPassword(registroRequest.getPassword()); // La encriptación se hace en el servicio
            nuevoUsuario.setNombres(registroRequest.getNombres());
            nuevoUsuario.setApellidos(registroRequest.getApellidos());
            nuevoUsuario.setEmail(registroRequest.getEmail());

            Usuario usuarioGuardado = usuarioService.registrarUsuario(nuevoUsuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            // Manejo de error si el username ya existe o similar
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar: " + e.getMessage());
        }
    }

    // LOGIN DE USUARIO (USANDO SEGURIDAD BASE)
    // Nota: Con Spring Security básico, una vez configurado, el login se gestiona a menudo por
    // el filtro de seguridad en el endpoint /login por defecto.
    // Aquí implementamos un esqueleto, pero la lógica de autenticación real
    // se maneja en la capa de seguridad.

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequest loginRequest) {
        // En una implementación real (con JWT), aquí se haría la autenticación
        // y se devolvería un token.

        // Para esta implementación base:
        // Solo verificamos si el usuario existe (la validación de password la haría Spring Security)
        return usuarioService.findByUsername(loginRequest.getUsername())
                .map(usuario -> {
                    // Si el usuario existe, se asume éxito (la seguridad real valida la pass)
                    return ResponseEntity.ok("Login exitoso. El sistema debe enviar credenciales o un JWT.");
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos."));
    }
}
