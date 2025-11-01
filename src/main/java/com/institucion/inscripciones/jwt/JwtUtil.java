package com.institucion.inscripciones.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final SecretKey key;
  private final long expirationMs;

  public JwtUtil(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.expiration-min}") long expMin) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expMin * 60_000;
  }

  public String generate(String username, Map<String, Object> claims) {
    Instant now = Instant.now();
    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(expirationMs)))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean isValid(String token, String expectedUser) {
    var payload = Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload();
    boolean notExpired = payload.getExpiration().after(new Date());
    return notExpired && expectedUser.equals(payload.getSubject());
  }
}