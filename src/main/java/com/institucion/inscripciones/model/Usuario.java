package com.institucion.inscripciones.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username; // Usado para login

    @Column(nullable = false, length = 255)
    private String password; // Contraseña hasheada

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Correo (puede ser gmail)

    @Column(nullable = false)
    private String rol = "ADMIN"; // Rol por defecto (puede ser USER, ADMIN, etc.)
}
