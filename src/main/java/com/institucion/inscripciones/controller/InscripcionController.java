package com.institucion.inscripciones.controller;

import com.institucion.inscripciones.dto.Inscripciones.InscripcionRequest;
import com.institucion.inscripciones.model.Alumno;
import com.institucion.inscripciones.model.Curso;
import com.institucion.inscripciones.model.Inscripcion;
import com.institucion.inscripciones.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    // MATRICULAR ALUMNO EN CURSO (POST)
    // Se espera un JSON como: {"alumnoId": 1, "cursoId": 5}
    @PostMapping("/matricular")
    public ResponseEntity<?> matricular(@RequestBody InscripcionRequest request) {
        Long alumnoId = request.getAlumnoId();
        Long cursoId = request.getCursoId();

        if (alumnoId == null || cursoId == null) {
            return ResponseEntity.badRequest().body("Se requieren 'alumnoId' y 'cursoId'.");
        }

        try {
            Inscripcion inscripcion = inscripcionService.matricularAlumnoEnCurso(request);
            return new ResponseEntity<>(inscripcion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ELIMINAR INSCRIPCION (Desmatricular)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        try {
            inscripcionService.eliminarInscripcion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ----------------------------------------------------------------------
    // REPORTES Y CONSULTAS

    // 1. Qué alumnos están matriculados en un curso.
    // GET /api/inscripciones/reporte/alumnos-por-curso/1
    @GetMapping("/reporte/alumnos-por-curso/{cursoId}")
    public ResponseEntity<List<Alumno>> obtenerAlumnosPorCurso(@PathVariable Long cursoId) {
        List<Alumno> alumnos = inscripcionService.obtenerAlumnosPorCurso(cursoId);
        return ResponseEntity.ok(alumnos);
    }

    // 2. A qué curso está matriculado un alumno.
    // GET /api/inscripciones/reporte/cursos-por-alumno/1
    @GetMapping("/reporte/cursos-por-alumno/{alumnoId}")
    public ResponseEntity<List<Curso>> obtenerCursosPorAlumno(@PathVariable Long alumnoId) {
        List<Curso> cursos = inscripcionService.obtenerCursosPorAlumno(alumnoId);
        return ResponseEntity.ok(cursos);
    }

    // -------------------- NUEVOS ENDPOINTS --------------------

    // INSCRIPCIONES por ALUMNO (lista de Inscripcion)
    // GET /api/inscripciones/por-alumno/1
    @GetMapping("/por-alumno/{alumnoId}")
    public ResponseEntity<List<Inscripcion>> inscripcionesPorAlumno(@PathVariable Long alumnoId) {
        return ResponseEntity.ok(inscripcionService.buscarInscripcionesPorAlumno(alumnoId));
    }

    // INSCRIPCIONES por CURSO (lista de Inscripcion)
    // GET /api/inscripciones/por-curso/5
    @GetMapping("/por-curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> inscripcionesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.buscarInscripcionesPorCurso(cursoId));
    }
}