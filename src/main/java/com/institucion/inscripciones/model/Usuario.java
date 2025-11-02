package com.institucion.inscripciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data @NoArgsConstructor @AllArgsConstructor
public class Usuario implements UserDetails {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String username;

  @Column(nullable = false, length = 255)
  private String password;  // guardá hash

  @Column(name="role",nullable = false)
  private String rol = "USER"; // USER o ADMIN

  // --- UserDetails ---
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Spring espera "ROLE_*"
    String roleName = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;
    return List.of(new SimpleGrantedAuthority(roleName));
  }
  @Override public boolean isAccountNonExpired()  { return true; }
  @Override public boolean isAccountNonLocked()   { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled()            { return true; }
}