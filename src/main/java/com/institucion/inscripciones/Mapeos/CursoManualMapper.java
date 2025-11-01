package com.institucion.inscripciones.Mapeos;

import org.springframework.stereotype.Component;

import com.institucion.inscripciones.dto.Cursos.CursoRequest;
import com.institucion.inscripciones.model.Curso;

@Component
public class CursoManualMapper {
  public Curso toEntity(CursoRequest dto) {
    Curso c = new Curso();
    c.setNombre(dto.getNombre());
    c.setDescripcion(dto.getDescripcion());
    c.setCreditos(dto.getCreditos());
    return c;
  }
}
