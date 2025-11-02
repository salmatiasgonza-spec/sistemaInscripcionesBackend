package com.institucion.inscripciones.service;

import com.institucion.inscripciones.Mapeos.CursoManualMapper;
import com.institucion.inscripciones.dto.Cursos.CursoRequest;
import com.institucion.inscripciones.model.Curso;
import com.institucion.inscripciones.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoManualMapper mapper;

    // Inyección por constructor
    public CursoService(CursoRepository cursoRepository, CursoManualMapper mapper) {
        this.cursoRepository = cursoRepository;
        this.mapper = mapper;
    }

    // CREAR/GUARDAR CURSO
    public Curso crearCurso(CursoRequest curso) {
        Curso entidad = mapper.toEntity(curso);
        return cursoRepository.save(entidad);
    }

    // MOSTRAR/OBTENER TODOS LOS CURSOS
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    // OBTENER CURSO POR ID
    public Optional<Curso> obtenerPorId(Long id) {
        return cursoRepository.findById(id);
    }

    // ACTUALIZAR CURSO
    public Curso actualizarCurso(Long id, CursoRequest detallesCurso) {
        return cursoRepository.findById(id).map(cursoExistente -> {
            cursoExistente.setNombre(detallesCurso.getNombre());
            cursoExistente.setDescripcion(detallesCurso.getDescripcion());
            cursoExistente.setCreditos(detallesCurso.getCreditos());
            return cursoRepository.save(cursoExistente);
        }).orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    // ELIMINAR CURSO
    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}