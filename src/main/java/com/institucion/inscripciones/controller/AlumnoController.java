package com.institucion.inscripciones.controller;

import com.institucion.inscripciones.dto.Alumnos.AlumnoRequest;
import com.institucion.inscripciones.model.Alumno;
import com.institucion.inscripciones.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    // CREAR ALUMNO (POST)
    @PostMapping
    public ResponseEntity<Alumno> crearAlumno(@RequestBody AlumnoRequest alumno) {
        Alumno nuevoAlumno = alumnoService.crearAlumno(alumno);
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);
    }

    // MOSTRAR TODOS LOS ALUMNOS (GET)
    @GetMapping
    public ResponseEntity<List<Alumno>> obtenerTodos() {
        List<Alumno> alumnos = alumnoService.obtenerTodos();
        return ResponseEntity.ok(alumnos);
    }

    // MOSTRAR ALUMNO POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerPorId(@PathVariable Long id) {
        return alumnoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ACTUALIZAR ALUMNO (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoRequest detallesAlumno) {
        try {
            Alumno alumnoActualizado = alumnoService.actualizarAlumno(id, detallesAlumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR ALUMNO (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long id) {
        try {
            alumnoService.eliminarAlumno(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
