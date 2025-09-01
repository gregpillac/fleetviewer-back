# ---------- Étape 0 : variables réutilisables ----------
ARG JDK_IMAGE=maven:3.9.6-eclipse-temurin-21
ARG JRE_IMAGE=eclipse-temurin:21-jre

# ---------- Étape 1 : build (cache Maven optimisé) ----------
FROM ${JDK_IMAGE} AS build
WORKDIR /app

# 1) Pré-copie des fichiers de config pour "chauffer" le cache Maven
#    (si tu utilises le wrapper Maven, copie aussi .mvn et mvnw)
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw* ./

# Télécharge les deps hors-ligne -> builds plus rapides
RUN ./mvnw -B -q -DskipTests dependency:go-offline || mvn -B -q -DskipTests dependency:go-offline

# 2) Copie du code et build du JAR
COPY src/ src/
RUN ./mvnw -B -q -DskipTests clean package || mvn -B -q -DskipTests clean package

# ---------- Étape 2 : runtime léger ----------
FROM ${JRE_IMAGE}
WORKDIR /app

# Sécurité : utilisateur non-root
RUN useradd -m spring && chown -R spring:spring /app
USER spring

# Localisation/encodage (logs lisibles, JSON correct)
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    TZ=Europe/Paris

# Options JVM raisonnables pour Render (mémoire & CPU dynamiques)
ENV JAVA_TOOL_OPTIONS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -XX:ActiveProcessorCount=2 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/urandom"

# Copie du JAR construit à l’étape build
COPY --from=build /app/target/*.jar app.jar

# Démarrage : Render injecte $PORT -> on le passe à Spring
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
