package com.institucion.inscripciones.repository;

import com.institucion.inscripciones.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // 1. REPORTES: Qué alumnos están matriculados en un curso
    // Se pide el curso_id de la tabla de Inscripciones
    List<Inscripcion> findByCursoId(Long cursoId);

    // 2. REPORTES: A qué curso está matriculado un alumno
    // Se pide el alumno_id de la tabla de Inscripciones
    List<Inscripcion> findByAlumnoId(Long alumnoId);

    // Para evitar duplicados: verificar si un alumno ya está inscrito en un curso
    boolean existsByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);
}
