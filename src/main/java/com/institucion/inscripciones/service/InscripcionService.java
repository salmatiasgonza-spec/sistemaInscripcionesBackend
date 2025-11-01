package com.institucion.inscripciones.service;

import com.institucion.inscripciones.dto.Inscripciones.InscripcionRequest;
import com.institucion.inscripciones.model.Alumno;
import com.institucion.inscripciones.model.Curso;
import com.institucion.inscripciones.model.Inscripcion;
import com.institucion.inscripciones.repository.AlumnoRepository;
import com.institucion.inscripciones.repository.CursoRepository;
import com.institucion.inscripciones.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // MATRICULAR UN ALUMNO EN UN CURSO
    public Inscripcion matricularAlumnoEnCurso(InscripcionRequest inscripcion) {
        if (inscripcionRepository.existsByAlumnoIdAndCursoId(inscripcion.getAlumnoId(), inscripcion.getCursoId())) {
            throw new RuntimeException("El alumno ya está matriculado en este curso.");
        }

        Alumno alumno = alumnoRepository.findById(inscripcion.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado."));

        Curso curso = cursoRepository.findById(inscripcion.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado."));

        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setAlumno(alumno);
        nuevaInscripcion.setCurso(curso);

        return inscripcionRepository.save(nuevaInscripcion);
    }

    // CONSULTA 1: QUÉ ALUMNOS ESTÁN MATRICULADOS EN UN CURSO
    public List<Alumno> obtenerAlumnosPorCurso(Long cursoId) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);

        // Mapear las inscripciones para obtener solo la lista de Alumnos
        return inscripciones.stream()
                .map(Inscripcion::getAlumno)
                .collect(Collectors.toList());
    }

    // CONSULTA 2: A QUÉ CURSO ESTÁ MATRICULADO UN ALUMNO
    public List<Curso> obtenerCursosPorAlumno(Long alumnoId) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByAlumnoId(alumnoId);

        // Mapear las inscripciones para obtener solo la lista de Cursos
        return inscripciones.stream()
                .map(Inscripcion::getCurso)
                .collect(Collectors.toList());
    }

    // ELIMINAR INSCRIPCION (Desmatricular)
    public void eliminarInscripcion(Long inscripcionId) {
        if (!inscripcionRepository.existsById(inscripcionId)) {
            throw new RuntimeException("Inscripción no encontrada.");
        }
        inscripcionRepository.deleteById(inscripcionId);
    }
}
