# --------- Étape 1 : build WAR avec Maven (JDK 21) ---------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw* ./
RUN ./mvnw -B -q -DskipTests dependency:go-offline || mvn -B -q -DskipTests dependency:go-offline
COPY src/ src/
RUN ./mvnw -B -q -DskipTests clean package || mvn -B -q -DskipTests clean package

# --------- Étape 2 : Tomcat 10 (JDK 21) ---------
FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat

# Nettoyage des apps par défaut
RUN rm -rf webapps/*

# On déploie le WAR comme application ROOT
COPY --from=build /app/target/*.war webapps/ROOT.war

# (Optionnel) logs lisibles + mémoire
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8 TZ=Europe/Paris \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -Dfile.encoding=UTF-8"

# IMPORTANT : Render impose $PORT, on le pousse dans server.xml avant de lancer Tomcat
CMD sh -c "sed -ri 's/port=\"8080\"/port=\"'\"$PORT\"'\"/' conf/server.xml && catalina.sh run"
