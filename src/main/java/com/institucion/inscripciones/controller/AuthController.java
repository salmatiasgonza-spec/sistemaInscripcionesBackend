package com.institucion.inscripciones.controller;

import com.institucion.inscripciones.dto.LoginRequest;
import com.institucion.inscripciones.dto.RegistroRequest;
import com.institucion.inscripciones.jwt.JwtUtil;
import com.institucion.inscripciones.model.Usuario;
import com.institucion.inscripciones.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;
  private final UsuarioService usuarioService;

  @PostMapping("/registrar")
  public ResponseEntity<?> registrar(@RequestBody RegistroRequest req) {
    Usuario u = new Usuario();
    u.setUsername(req.getUsername());
    u.setPassword(req.getPassword());
    if (req.getRol() != null) u.setRol(req.getRol());
    Usuario saved = usuarioService.registrarUsuario(u);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    var tokenReq = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
    authManager.authenticate(tokenReq); // lanza excepción si falla
    String jwt = jwtUtil.generate(req.getUsername(), Map.of("typ","access"));
    return ResponseEntity.ok(Map.of("token", jwt));
  }
}