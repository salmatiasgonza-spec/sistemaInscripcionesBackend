package com.institucion.inscripciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean para el encriptador de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración del Filtro de Seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (necesario para APIs REST sin estado)
                .csrf(AbstractHttpConfigurer::disable)
                // Permitir CORS (necesario para frontend React en otro puerto)
                .cors(withDefaults())

                // Reglas de autorización para los endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir acceso sin autenticación a H2 console y el endpoint de registro/login
                        .requestMatchers("/h2-console/**", "/api/auth/registrar", "/api/auth/login").permitAll()

                        // TODOS los demás endpoints (CRUD de alumnos, cursos, inscripciones y reportes)
                        // REQUIEREN autenticación.
                        .anyRequest().authenticated()
                )

                // Configuración básica de autenticación HTTP (pop-up de navegador)
                // En un proyecto React/producción, esto se reemplazaría por JWT.
                .httpBasic(withDefaults())

                // Configuración para permitir que H2 Console funcione con iframes
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
