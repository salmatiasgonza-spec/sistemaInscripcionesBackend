package com.institucion.inscripciones.controller;

import com.institucion.inscripciones.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

  private final ReportService report;

  public ReporteController(ReportService report) {
    this.report = report;
  }

  @GetMapping(value = "/alumnos", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> alumnos() {
    return pdf(report.alumnosPdf(), "alumnos.pdf");
  }

  @GetMapping(value = "/cursos", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> cursos() {
    return pdf(report.cursosPdf(), "cursos.pdf");
  }

  @GetMapping(value = "/inscripciones", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> inscripciones() {
    return pdf(report.inscripcionesPdf(), "inscripciones.pdf");
  }

  @GetMapping(value = "/alumno/{id}/cursos", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> cursosDeAlumno(@PathVariable Long id) {
    return pdf(report.cursosDeAlumnoPdf(id), "cursos_alumno_" + id + ".pdf");
  }

  @GetMapping(value = "/curso/{id}/alumnos", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> alumnosDeCurso(@PathVariable Long id) {
    return pdf(report.alumnosDeCursoPdf(id), "alumnos_curso_" + id + ".pdf");
  }

  private ResponseEntity<byte[]> pdf(byte[] bytes, String filename) {
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
      .contentType(MediaType.APPLICATION_PDF)
      .body(bytes);
  }
}