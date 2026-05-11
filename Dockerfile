# ─────────────────────────────────────────────
# Stage 1: Build the Spring Boot JAR with Gradle
# (jammy = Ubuntu LTS, supports ARM64 + AMD64)
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copy Gradle wrapper and config first (layer cache)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies in a separate layer (cache friendly)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the JAR (bootJar is configured to output as app.jar in root)
RUN ./gradlew bootJar --no-daemon -x test

# ─────────────────────────────────────────────
# Stage 2: Run the JAR with a minimal JRE image
# (jammy = Ubuntu LTS, supports ARM64 + AMD64)
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create a non-root user for security (VAPT requirement)
RUN groupadd -r shellapp && useradd -r -g shellapp shellapp

# Copy the built JAR from Stage 1
COPY --from=builder /app/app.jar app.jar

# Set ownership to non-root user
RUN chown shellapp:shellapp app.jar

USER shellapp

EXPOSE 8080

# JVM tuning for containerized environments
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
