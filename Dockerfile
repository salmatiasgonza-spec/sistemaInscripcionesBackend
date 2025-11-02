# --- Etapa 1: Build con Gradle y JDK 21 ---
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# --- Etapa 2: Runtime con JDK 21 slim ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el JAR "fat" (no el *-plain.jar*)
COPY --from=build /app/build/libs/sistema-inscripciones-backend-0.0.1-SNAPSHOT.jar app.jar

# Render expone $PORT. Forzamos Spring a escuchar  ahí.
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
CMD ["sh","-c","java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]