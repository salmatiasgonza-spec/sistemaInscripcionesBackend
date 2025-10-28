package com.institucion.inscripciones.dto;

import com.institucion.inscripciones.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {
    private String username;
    private String password;
    private String nombres;
    private String apellidos;
    private String email;
    // El rol será por defecto "ADMIN" o "USER" según la lógica de negocio.
}
