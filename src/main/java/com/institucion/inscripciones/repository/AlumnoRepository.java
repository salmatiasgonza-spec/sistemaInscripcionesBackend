package com.institucion.inscripciones.repository;

import com.institucion.inscripciones.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Permite buscar por CI o Email si se necesita en el futuro
    // Optional<Alumno> findByCi(String ci);
    // Optional<Alumno> findByEmail(String email);
}
