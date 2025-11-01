package com.institucion.inscripciones.dto;
import lombok.Data;

@Data
public class RegistroRequest {
  private String username;
  private String password;
  private String rol; // opcional: "USER" o "ADMIN"
}