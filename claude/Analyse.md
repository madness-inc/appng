# appNG – Projektanalyse

## Überblick

**appNG** ist eine Java-basierte Web Application Platform und Framework, entwickelt von der aiticon GmbH. Das Projekt ist als Maven Multi-Modul-Projekt organisiert und steht unter der Apache 2.0 Lizenz.

| Eigenschaft      | Wert                          |
|------------------|-------------------------------|
| groupId          | org.appng                     |
| artifactId       | appng-parent                  |
| Version          | 1.26.6-SNAPSHOT               |
| Lizenz           | Apache 2.0                    |
| Gründungsjahr    | 2011                          |
| Website          | https://appng.org             |
| Organisation     | aiticon GmbH                  |

### Entwickler

| Name                 | GitHub-ID    |
|----------------------|--------------|
| Matthias Herlitzius  | mherlitzius  |
| Matthias Müller      | madness-inc  |
| Claus Stümke         | stuemke      |

---

## Technologie-Stack

| Kategorie          | Technologie / Version                      |
|--------------------|--------------------------------------------|
| Laufzeit           | Apache Tomcat 9.0.31 (mind. 8.5.x)        |
| Framework          | Spring Framework 4.3.30.RELEASE            |
| ORM                | Hibernate 5.4.32.Final                     |
| Persistenz         | Spring Data JPA 1.11.23 / Envers 1.1.23   |
| Datenbank          | MySQL ≥ 5.6 (HikariCP Connection Pool)     |
| Migrationen        | Flyway 8.5.13                              |
| Suche              | Apache Lucene 8.10.0                       |
| Caching            | Hazelcast 5.3.0                            |
| Query-DSL          | QueryDSL 5.0.0                             |
| JSON               | Jackson 2.14.1                             |
| BPMN               | Camunda 7.15.0                             |
| Security           | BouncyCastle 1.69, ESAPI 2.2.3.1           |
| REST               | OpenAPI/Swagger (openapi-generator)        |
| Logging            | SLF4J 1.7.26                               |
| Java               | JRE ≥ 1.8                                  |

---

## Modulstruktur (27 Module)

### Kern-Module

| Modul                   | Packaging | Beschreibung                                                        |
|-------------------------|-----------|---------------------------------------------------------------------|
| `appng-tools`           | jar       | Allgemeine Hilfsfunktionen (Jackson, Commons, im4java, POI)         |
| `appng-mail`            | jar       | E-Mail-Versand via Jakarta Mail                                     |
| `appng-forms`           | jar       | Formularverarbeitung (Tomcat, ESAPI, JSoup)                         |
| `appng-formtags`        | jar       | JSP-Tag-Library für Formulare                                       |
| `appng-xmlapi`          | jar       | XML-API (XSD→Java per JAXB, Saxon)                                  |
| `appng-persistence`     | jar       | Persistenzschicht (Spring Data JPA, Hibernate, Envers, QueryDSL)   |
| `appng-api`             | jar       | Öffentliche appNG-API (Message Constants, Spring, Jackson)          |
| `appng-rest-api`        | jar       | REST-API (Swagger/OpenAPI Codegenerator)                            |
| `appng-search`          | jar       | Volltextsuche (Lucene, Tika, POI, PDFBox, JSoup)                   |
| `appng-core`            | jar       | Kernbibliothek (Spring, Hibernate, QueryDSL, TestContainers)        |
| `appng-cli`             | jar       | Kommandozeilenwerkzeug (JCommander)                                 |
| `appng-taglib`          | jar       | JSP-Tag-Library (appng-core, appng-search, appng-formtags)         |
| `appng-camunda`         | jar       | Camunda BPMN Integration (camunda-engine-spring, Lombok)            |

### Anwendungs-Module

| Modul                       | Packaging | Beschreibung                                          |
|-----------------------------|-----------|-------------------------------------------------------|
| `appng-application`         | war       | Haupt-WAR mit allen Core-Modulen                      |
| `appng-application-camunda` | war       | WAR inkl. Camunda BPMN Process Engine                 |
| `appng-upngizr`             | war       | Privilegierte Web-App zum Updaten von appNG           |

### Build & Infrastruktur

| Modul                          | Packaging     | Beschreibung                                                |
|--------------------------------|---------------|-------------------------------------------------------------|
| `appng-maven-plugin`           | maven-plugin  | Generiert Konstanten-Klassen aus Properties/application.xml |
| `appng-appngizer-jaxb`         | jar           | JAXB-API für appNGizer (aus appngizer.xsd generiert)        |
| `appng-appngizer`              | jar           | REST-Management-API für appNG                               |
| `appng-appngizer-maven-plugin` | maven-plugin  | Maven-Plugin für appNGizer-Operationen                      |
| `appng-archetype-application`  | jar           | Maven-Archetype für neue appNG-Anwendungen                  |

### Eltern-POMs & Assemblies

| Modul                       | Packaging | Beschreibung                                         |
|-----------------------------|-----------|------------------------------------------------------|
| `appng-application-bom`     | pom       | Bill of Materials mit 500+ verwalteten Dependencies  |
| `appng-application-parent`  | pom       | Basis-POM für appNG-Anwendungen                      |
| `appng-template-parent`     | pom       | Basis-POM für appNG-Templates                        |
| `appng-application-assembly`| jar       | Assembly-Deskriptoren für Anwendungen                |
| `appng-template-assembly`   | jar       | Assembly-Deskriptoren für Templates                  |
| `appng-testsupport`         | jar       | Test-Infrastruktur (JUnit, DBUnit, Mockito, Spring)  |

### Dokumentation

| Modul                  | Packaging | Beschreibung                                                |
|------------------------|-----------|-------------------------------------------------------------|
| `appng-documentation`  | pom       | AsciiDoc-Dokumentation (HTML + PDF via Asciidoctor)         |

---

## Kernfunktionalitäten (laut README)

- **MVC-Paradigm** mit deklarativer XML-basierter UI-Definition
- **Business Logic** über Java-Interfaces
- **Automatisches Parameter-Binding**
- **Paging, Filtering, Sorting** out-of-the-box
- **JPA / Spring Data** Persistenz
- **HikariCP** Connection Pooling
- **Flyway** Datenbankmigrationen
- **Role-Based Access Control** (RBAC)
- **SOAP/REST Webservices**
- **i18n** Internationalisierung
- **Hazelcast** Caching und Clustering
- **Lucene** Volltextindexierung und -suche
- **Quartz** Job Scheduling
- **Camunda BPMN** Workflow-Unterstützung
- **Multi-Tenancy**

---

## Maven-Profile

| Profil         | Zweck                                                          |
|----------------|----------------------------------------------------------------|
| `maven-central`| GPG-Signierung und Sonatype-Publishing (Maven Central Release) |
| `javadocs`     | JavaDoc-Generierung                                            |
| `ci`           | CI-spezifisches Maven-Repository                               |
| `sonar`        | JaCoCo Code-Coverage für SonarQube                            |
| `local`        | Entwicklungs-Profil: kopiert JARs in lokale Tomcat-Instanz    |
| `default`      | Standard: baut alle 27 Module                                  |

---

## README-Dateien

| Datei                                  | Inhalt                                                                      |
|----------------------------------------|-----------------------------------------------------------------------------|
| `README.adoc`                          | Plattform-Überblick, Features, Einstiegspunkt                               |
| `appng-application/README.adoc`        | Installation (Tomcat, MySQL), Deployment-Anleitung                          |
| `appng-maven-plugin/README.adoc`       | Plugin-Konfiguration, Konstanten-Generierung, m2e-Eclipse-Integration       |
| `appng-upngizr/README.adoc`            | upNGizr-Konfiguration (buildRepository, replaceBin, blockRemoteIPs, useFQDN)|
| `appng-application-camunda/README.adoc`| Camunda-Integration, WAR-Download-Links, Developer Guide Referenz           |

---

## Deployment-Anforderungen

- **JRE:** ≥ 1.8
- **Applikationsserver:** Apache Tomcat 8.5.x oder 9.x
- **Datenbank:** MySQL ≥ 5.6.x
- **Build-Tool:** Apache Maven (mit Java ≥ 8)

---

## Verteilungskanäle

- **Maven Central** (via Sonatype OSSRH) – offizielle Releases
- **GitHub Releases** – WAR-Artefakte für `appng-application` und `appng-application-camunda`
- **SCM:** https://github.com/appNG/appng

---

## Hinweise zur Codebase

- Die Root-POM (`pom.xml`) ist mit ~1200 Zeilen das zentrale Dependency-Management-Dokument.
- `appng-application-bom` enthält eine Bill of Materials mit über 500 verwalteten Abhängigkeiten (~1500 Zeilen).
- JAXB-Code wird zur Build-Zeit aus XSD-Schemata generiert (appng-xmlapi, appng-appngizer-jaxb).
- Die REST-API wird per Swagger/OpenAPI-Generator aus einer Spec-Datei generiert.
- Es existiert keine `CLAUDE.md` im Projekt.
