package com.institucion.inscripciones.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {

    String path = request.getRequestURI();

    // 1) Dejar pasar sin tocar: auth, swagger, error y preflight CORS
    if (path.startsWith("/api/auth/")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/swagger-ui")
        || path.equals("/swagger-ui.html")
        || path.equals("/error")
        || HttpMethod.OPTIONS.matches(request.getMethod())) {
      chain.doFilter(request, response);
      return;
    }

    // 2) Solo procesar si hay Bearer
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    String username;
    try {
      username = jwtUtil.extractUsername(token);
    } catch (Exception e) {
      chain.doFilter(request, response);
      return;
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      var user = userDetailsService.loadUserByUsername(username);
      if (jwtUtil.isValid(token, user.getUsername())) {
        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    chain.doFilter(request, response);
  }
}