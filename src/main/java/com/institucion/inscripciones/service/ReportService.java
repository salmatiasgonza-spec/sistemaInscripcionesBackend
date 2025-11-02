package com.institucion.inscripciones.service;

import com.institucion.inscripciones.model.Alumno;
import com.institucion.inscripciones.model.Curso;
import com.institucion.inscripciones.model.Inscripcion;
import com.institucion.inscripciones.repository.AlumnoRepository;
import com.institucion.inscripciones.repository.CursoRepository;
import com.institucion.inscripciones.repository.InscripcionRepository;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

  private final AlumnoRepository alumnoRepo;
  private final CursoRepository cursoRepo;
  private final InscripcionRepository inscRepo;

  public ReportService(AlumnoRepository a, CursoRepository c, InscripcionRepository i) {
    this.alumnoRepo = a; this.cursoRepo = c; this.inscRepo = i;
  }

  public byte[] alumnosPdf() {
    List<Alumno> data = alumnoRepo.findAll();
    return tablePdf("Listado de Alumnos",
      new String[]{"ID","Nombre","Apellido","Email"},
      data.stream().map(a -> new String[]{
        String.valueOf(a.getId()), a.getNombres(), a.getApellidos(), a.getEmail()
      }).toList());
  }

  public byte[] cursosPdf() {
    List<Curso> data = cursoRepo.findAll();
    return tablePdf("Listado de Cursos",
      new String[]{"ID","Nombre","Descripción","Créditos"},
      data.stream().map(c -> new String[]{
        String.valueOf(c.getId()), c.getNombre(), c.getDescripcion(), String.valueOf(c.getCreditos())
      }).toList());
  }

  public byte[] inscripcionesPdf() {
    List<Inscripcion> data = inscRepo.findAll();
    return tablePdf("Listado de Inscripciones",
      new String[]{"ID","Alumno","Curso","Fecha"},
      data.stream().map(i -> new String[]{
        String.valueOf(i.getId()),
        i.getAlumno().getNombres()+" "+i.getAlumno().getApellidos(),
        i.getCurso().getNombre(),
        String.valueOf(i.getFechaInscripcion())
      }).toList());
  }

  public byte[] cursosDeAlumnoPdf(Long alumnoId) {
    Alumno a = alumnoRepo.findById(alumnoId).orElseThrow();
    List<Inscripcion> data = inscRepo.findByAlumnoId(alumnoId);
    return tablePdf("Cursos del alumno: "+a.getNombres()+" "+a.getApellidos(),
      new String[]{"Curso","Créditos","Fecha"},
      data.stream().map(i -> new String[]{
        i.getCurso().getNombre(), String.valueOf(i.getCurso().getCreditos()), String.valueOf(i.getFechaInscripcion())
      }).toList());
  }

  public byte[] alumnosDeCursoPdf(Long cursoId) {
    Curso c = cursoRepo.findById(cursoId).orElseThrow();
    List<Inscripcion> data = inscRepo.findByCursoId(cursoId);
    return tablePdf("Alumnos del curso: "+c.getNombre(),
      new String[]{"Alumno","Email","Fecha"},
      data.stream().map(i -> new String[]{
        i.getAlumno().getNombres()+" "+i.getAlumno().getApellidos(),
        i.getAlumno().getEmail(),
        String.valueOf(i.getFechaInscripcion())
      }).toList());
  }

  private byte[] tablePdf(String title, String[] headers, List<String[]> rows) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
      PdfWriter.getInstance(doc, out);
      doc.open();

      Font h1 = new Font(Font.HELVETICA, 16, Font.BOLD);
      Font th = new Font(Font.HELVETICA, 10, Font.BOLD);
      Font td = new Font(Font.HELVETICA, 10);

      doc.add(new Paragraph(title, h1));
      doc.add(Chunk.NEWLINE);

      PdfPTable table = new PdfPTable(headers.length);
      table.setWidthPercentage(100);

      for (String h : headers) {
        PdfPCell cell = new PdfPCell(new Paragraph(h, th));
        cell.setGrayFill(0.9f);
        table.addCell(cell);
      }
      for (String[] r : rows) {
        for (String v : r) table.addCell(new Paragraph(v == null ? "" : v, td));
      }

      doc.add(table);
      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
    }
  }
}