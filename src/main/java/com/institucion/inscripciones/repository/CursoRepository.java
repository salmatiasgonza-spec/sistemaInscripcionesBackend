package com.institucion.inscripciones.repository;

import com.institucion.inscripciones.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // Optional<Curso> findByNombre(String nombre);
}
