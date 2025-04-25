# fleetviewer-back
Services backend d'authentification de l'application FleetViewer (dev collectif MS2D25)

## Prérequis
- Installer [JDK 21](https://www.oracle.com/fr/java/technologies/downloads/#jdk21-windows)
- Installer [Postgresql 17.4](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
- Créer une base de données via PgAdmin (installé avec pgsql)

## Configurer les properties
Créer le fichier application-local.properties dans src/main/resources

```bash
# Configuration de la base de données PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/[nom-bdd]
spring.datasource.username=[nom]
spring.datasource.password=[mdp]
spring.datasource.driver-class-name=org.postgresql.Driver

