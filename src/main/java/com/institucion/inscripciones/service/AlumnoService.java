package com.institucion.inscripciones.service;

import com.institucion.inscripciones.model.Alumno;
import com.institucion.inscripciones.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    // CREAR/GUARDAR ALUMNO
    public Alumno crearAlumno(Alumno alumno) {
        // Podrías añadir validaciones aquí (e.g., CI no duplicada)
        return alumnoRepository.save(alumno);
    }

    // MOSTRAR/OBTENER TODOS LOS ALUMNOS
    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }

    // OBTENER ALUMNO POR ID
    public Optional<Alumno> obtenerPorId(Long id) {
        return alumnoRepository.findById(id);
    }

    // ACTUALIZAR ALUMNO
    public Alumno actualizarAlumno(Long id, Alumno detallesAlumno) {
        return alumnoRepository.findById(id).map(alumnoExistente -> {
            alumnoExistente.setNombres(detallesAlumno.getNombres());
            alumnoExistente.setApellidos(detallesAlumno.getApellidos());
            alumnoExistente.setCi(detallesAlumno.getCi());
            alumnoExistente.setEmail(detallesAlumno.getEmail());
            return alumnoRepository.save(alumnoExistente);
        }).orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
    }

    // ELIMINAR ALUMNO
    public void eliminarAlumno(Long id) {
        if (!alumnoRepository.existsById(id)) {
            throw new RuntimeException("Alumno no encontrado con ID: " + id);
        }
        alumnoRepository.deleteById(id);
    }
}
