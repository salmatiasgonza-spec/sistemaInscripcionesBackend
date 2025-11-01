package com.institucion.inscripciones.dto.Alumnos;

import lombok.Data;

@Data
public class AlumnoRequest {

    private String nombres;
    private String apellidos;
    private String ci; // Cédula de Identidad
    private String email; // Gmail o cualquier otro

}