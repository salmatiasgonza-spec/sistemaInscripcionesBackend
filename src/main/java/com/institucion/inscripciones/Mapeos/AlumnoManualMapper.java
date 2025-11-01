package com.institucion.inscripciones.Mapeos;

import org.springframework.stereotype.Component;

import com.institucion.inscripciones.dto.Alumnos.AlumnoRequest;
import com.institucion.inscripciones.model.Alumno;

@Component
public class AlumnoManualMapper {
  public Alumno toEntity(AlumnoRequest dto) {
    Alumno a = new Alumno();
    a.setNombres(dto.getNombres());
    a.setApellidos(dto.getApellidos());
    a.setCi(dto.getCi());
    a.setEmail(dto.getEmail());
    return a;
  }
}
