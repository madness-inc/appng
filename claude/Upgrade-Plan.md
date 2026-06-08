# appNG – Dependency-Upgrade-Plan

> Stand: Juni 2026 | Basis: appNG 1.26.6-SNAPSHOT

---

## Executive Summary

Das Projekt baut auf einem Technologie-Stack auf, der seit mehreren Jahren EOL ist:
Spring 4.3 hat seit Dezember 2020 keine Sicherheitspatches mehr erhalten, Hibernate 5.4 ist
ebenfalls abgekündigt, und `log4j 1.2.17` hat aktiv ausgenutzte, ungepatchte CVEs.

Die Hauptherausforderung ist der **javax → jakarta Namespace-Wechsel**: Spring 6+,
Hibernate 6+, Tomcat 10+ und Jakarta EE 9+ haben den `javax.*`-Namensraum vollständig
zu `jakarta.*` umbenannt. Das betrifft hunderte von `import`-Statements im Quellcode und
alle XML-Konfigurationsdateien. Ein inkrementeller Ansatz in klar abgegrenzten Phasen
reduziert das Risiko erheblich.

---

## Versionsübersicht: Alt → Neu

### Kern-Frameworks

| Abhängigkeit | Aktuell | Neueste stabile | Zielversion | Aufwand |
|---|---|---|---|---|
| **Spring Framework** | 4.3.30.RELEASE | 7.0.7 | **6.2.x** → spätere 7.x | Sehr hoch |
| **Spring Data JPA** | 1.11.23.RELEASE | 4.0.5 | **3.5.x** | Sehr hoch |
| **Spring Data Envers** | 1.1.23.RELEASE | 4.0.5 | **3.5.x** | Hoch |
| **Spring Data Commons** | 1.13.23.RELEASE | 4.0.5 | **3.5.x** | Hoch |
| **Spring Security** | 4.2.17.RELEASE | 7.0.4 | **6.5.x** | Sehr hoch |
| **Spring WS** | 2.4.6.RELEASE | 4.0.x | **4.0.x** | Hoch |
| **Hibernate ORM** | 5.4.32.Final | 7.4.0.Final | **6.6.x** | Sehr hoch |
| **Hibernate Validator** | 6.2.0.Final | 9.1.0.Final | **8.0.x** | Mittel |
| **Apache Tomcat** | 9.0.31 | 11.0.22 | **9.0.118** → **10.1.x** | Sehr hoch |
| **Jackson** | 2.14.1 | 2.19.x | **2.19.x** | Niedrig |
| **Apache Lucene** | 8.10.0 | 10.4.0 | **9.12.x** → 10.x | Hoch |
| **Flyway** | 8.5.13 | 12.7.0 | **10.x** | Mittel |
| **Hazelcast** | 5.3.0 | 5.6.0 | **5.6.0** | Niedrig |
| **QueryDSL** | 5.0.0 | 5.1.0 | **5.1.0** | Niedrig |
| **Camunda BPM** | 7.15.0 | 7.24.0 (CE EOL) | **7.24.0** | Mittel |

### Logging & Security

| Abhängigkeit | Aktuell | Neueste stabile | Zielversion | Aufwand |
|---|---|---|---|---|
| **Log4j 1.x** ⚠️ KRITISCH | 1.2.17 | — (abgekündigt) | **Entfernen** → SLF4J+Logback | Mittel |
| **SLF4J** | 1.7.26 | 2.0.18 | **2.0.18** | Niedrig |
| **Logback** | — | 1.5.34 | **1.5.34** | Niedrig |
| **BouncyCastle** | 1.69 (`jdk15on`) | 1.84 (`jdk18on`) | **1.84** (`jdk18on`) | Niedrig |
| **OWASP ESAPI** | 2.2.3.1 | 2.7.0.0 | **2.7.0.0** | Niedrig |

### Persistenz & Datenbank

| Abhängigkeit | Aktuell | Neueste stabile | Zielversion | Aufwand |
|---|---|---|---|---|
| **MySQL Connector** | 8.0.28 (`mysql:mysql-connector-java`) | 9.3.x | **9.x** (`com.mysql:mysql-connector-j`) | Niedrig |
| **HikariCP** | 4.0.3 | 7.0.2 | **6.x** (Java 17) | Niedrig |
| **HSQLDB** (Test) | 2.5.0 | 2.7.4 | **2.7.4** | Niedrig |
| **DBUnit** (Test) | 2.6.0 | 2.8.0 | **2.8.0** | Niedrig |

### Dokumenten-Verarbeitung & Suche

| Abhängigkeit | Aktuell | Neueste stabile | Zielversion | Aufwand |
|---|---|---|---|---|
| **Apache Tika** | 1.18 | 3.3.1 | **2.9.x** → 3.x | Mittel |
| **Apache PDFBox** | 2.0.24 | 3.0.7 | **3.0.7** | Mittel |
| **Apache POI** | 3.17 | 5.5.1 | **5.5.1** | Mittel |
| **JSoup** | 1.15.3 | 1.22.2 | **1.22.2** | Niedrig |

### Weitere Libraries & Tools

| Abhängigkeit | Aktuell | Neueste stabile | Zielversion | Aufwand |
|---|---|---|---|---|
| **Lombok** | 1.16.22 | 1.18.46 | **1.18.46** | Niedrig |
| **JCommander** | 1.72 (`com.beust`) | 1.82 | **1.82** (`com.beust`) | Niedrig |
| **Quartz Scheduler** | 2.3.2 | 2.5.2 | **2.5.2** | Niedrig |
| **Saxon-HE** | 10.6 | 12.x | **12.x** | Niedrig |
| **Freemarker** | 2.3.31 | 2.3.33 | **2.3.33** | Niedrig |
| **Guava** | 31.0.1-jre | 33.x | **33.x** | Niedrig |
| **Mockito** | 1.10.19 (`mockito-all`) | 5.23.0 | **5.x** | Mittel |
| **JUnit** | 4.13.2 | 5.14.4 | **5.x** | Mittel |

### Jakarta-Namespace-Artefakte

| Abhängigkeit | Aktuell | Ziel | Aufwand |
|---|---|---|---|
| **Servlet API** | `javax.servlet:javax.servlet-api` 3.1.0 | `jakarta.servlet:jakarta.servlet-api` 6.0.0 | Hoch (Phase 3) |
| **JPA API** | `jakarta.persistence-api` 2.2.3 (JPA 2.2) | `jakarta.persistence-api` 3.2.0 (JPA 3.2) | Hoch (Phase 3) |
| **Bean Validation** | `jakarta.validation-api` 2.0.2 | `jakarta.validation-api` 3.1.1 | Mittel |
| **Jakarta Mail** | `com.sun.mail:jakarta.mail` 1.6.7 | `org.eclipse.angus:angus-mail` 2.0.4 | Mittel |
| **Jakarta Activation** | `jakarta.activation-api` 1.2.2 | `jakarta.activation-api` 2.1.3 | Niedrig |
| **Jakarta Annotation** | `jakarta.annotation-api` 1.3.5 | `jakarta.annotation-api` 3.0.0 | Niedrig |
| **Jakarta Inject** | `jakarta.inject-api` 1.0.3 | `jakarta.inject-api` 2.0.1 | Niedrig |
| **Jakarta WS-RS** | `jakarta.ws.rs-api` 2.1.6 | `jakarta.ws.rs-api` 4.0.0 | Niedrig |

### Maven-Plugins

| Plugin | Aktuell | Ziel | Aufwand |
|---|---|---|---|
| maven-compiler-plugin | 3.6.1 (source/target 1.8) | 3.13.0 (Java 17/21) | Niedrig |
| maven-assembly-plugin | 3.0.0 | 3.7.1 | Niedrig |
| maven-war-plugin | 3.1.0 | 3.4.0 | Niedrig |
| maven-javadoc-plugin | 2.10.4 | 3.11.2 | Niedrig |
| maven-surefire-plugin | 3.0.0-M5 | 3.5.3 | Niedrig |
| maven-jar-plugin | 3.0.2 | 3.4.2 | Niedrig |
| maven-resources-plugin | 3.0.2 | 3.3.1 | Niedrig |
| maven-dependency-plugin | 3.0.1 | 3.8.1 | Niedrig |
| jaxb2-maven-plugin | 1.6 | 3.2.0 | Mittel |
| asciidoctor-maven-plugin | 1.5.6 | 3.1.0 | Mittel |
| jacoco-maven-plugin | 0.8.5 | 0.8.13 | Niedrig |
| sonar-maven-plugin | 3.7.0.1746 | 5.0.0.4389 | Niedrig |
| nexus-staging-maven-plugin | 1.6.7 | → `central-publishing-maven-plugin` 0.7.0 | Niedrig |

---

## Kritische Sofortmaßnahmen (vor allem anderen!)

### 1. Log4j 1.x entfernen — SICHERHEITSKRITISCH

`log4j:log4j:1.2.17` hat **mehrere ungepatchte CVEs** (u.a. CVE-2019-17571,
CVE-2022-23302/23303/23305). Die Version ist seit 2015 abgekündigt und wird nie gepatcht.

```xml
<!-- ENTFERNEN: -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    ...
</dependency>

<!-- ERSETZEN DURCH: -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.34</version>
</dependency>
```

Zusätzlich alle `log4j.properties`-Dateien durch `logback.xml` ersetzen.

### 2. Mockito-All entfernen

`org.mockito:mockito-all:1.10.19` ist seit Jahren EOL und enthält gebundelte Abhängigkeiten,
die Konflikte verursachen.

```xml
<!-- ENTFERNEN: mockito-all -->
<!-- ERSETZEN DURCH: -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.23.0</version>
    <scope>test</scope>
</dependency>
```

---

## Phasenplan

### Phase 0 – Sofortmaßnahmen (Security, ~1 Woche)

**Ziel:** Kritische Sicherheitslücken schließen, ohne Breaking Changes einzuführen.  
**Java-Version:** bleibt 8/11

| Maßnahme | Aktion |
|---|---|
| Log4j 1.x entfernen | → Logback 1.2.x (SLF4J 1.7 kompatibel) |
| Tomcat 9.0.31 → 9.0.118 | Sicherheitspatch, kein Breaking Change |
| Jackson 2.14.1 → 2.19.x | Vollständig kompatibel |
| Hazelcast 5.3.0 → 5.6.0 | Vollständig kompatibel |
| BouncyCastle 1.69 → 1.84 | Artifact-Rename: `jdk15on` → `jdk18on` |
| ESAPI 2.2.3.1 → 2.7.0.0 | Vollständig kompatibel |
| Lombok 1.16.22 → 1.18.46 | Vollständig kompatibel |
| JSoup 1.15.3 → 1.22.2 | Vollständig kompatibel |
| QueryDSL 5.0.0 → 5.1.0 | Vollständig kompatibel |
| Quartz 2.3.2 → 2.5.2 | Vollständig kompatibel |
| Freemarker 2.3.31 → 2.3.33 | Vollständig kompatibel |
| Guava 31.0.1 → 33.x | Vollständig kompatibel |
| JUnit 4.13.2 → 5.x | JUnit 5 Vintage Engine einsetzen für Kompatibilität |
| Mockito 1.10.19 → 5.23.0 | `mockito-all` → `mockito-core` |
| Maven-Plugins aktualisieren | Alle Plugins auf aktuelle Versionen |

**BouncyCastle Artifact-Rename:**
```xml
<!-- ALT: -->
<groupId>org.bouncycastle</groupId>
<artifactId>bcprov-jdk15on</artifactId>

<!-- NEU: -->
<groupId>org.bouncycastle</groupId>
<artifactId>bcprov-jdk18on</artifactId>
```
Gleiches gilt für `bcpkix-jdk15on` → `bcpkix-jdk18on` und `bcmail-jdk15on` → `bcmail-jdk18on`.

---

### Phase 1 – Java-Version & einfache Upgrades (~2 Wochen)

**Ziel:** Auf Java 17 migrieren und alle non-jakarta-breaking Upgrades durchführen.  
**Java-Version:** 8 → **17**

```xml
<!-- pom.xml: maven-compiler-plugin -->
<source>17</source>
<target>17</target>
<!-- oder besser: -->
<release>17</release>
```

| Maßnahme | Details |
|---|---|
| **Java 17 als Zielversion** | `--release 17` im Compiler-Plugin; JVM-Args anpassen |
| SLF4J 1.7.26 → 2.0.18 | `StaticLoggerBinder` wird durch ServiceLoader-Mechanismus ersetzt |
| Logback → 1.5.34 | Erfordert SLF4J 2.x |
| Flyway 8.5.13 → 10.x | `cleanDisabled=true` jetzt Default; DB-Treiber als separate Artefakte |
| Apache POI 3.17 → 5.5.1 | Java 11 Minimum; API-interne Änderungen; Klassen-Reorganisation |
| Apache PDFBox 2.0.24 → 3.0.7 | API-Klassen reorganisiert |
| Saxon-HE 10.6 → 12.x | API-Änderungen; prüfen |
| MySQL Connector 8.0.28 → 9.x | groupId: `mysql` → `com.mysql`, artifactId: `mysql-connector-java` → `mysql-connector-j` |
| HikariCP 4.0.3 → 6.x | Java 11 Minimum |
| HSQLDB 2.5.0 → 2.7.4 | Kompatibel |
| DBUnit 2.6.0 → 2.8.0 | Kompatibel |

**Flyway-Anpassungen:**
```xml
<!-- flyway-core allein reicht nicht mehr für MySQL -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>10.x.x</version>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
    <version>10.x.x</version>
</dependency>
```

**MySQL Connector:**
```xml
<!-- ALT: -->
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>

<!-- NEU: -->
<groupId>com.mysql</groupId>
<artifactId>mysql-connector-j</artifactId>
```

---

### Phase 2 – Dokumenten-Verarbeitung & Suche (~2 Wochen)

**Ziel:** Tika und Lucene upgraden (beide erfordern Code-Anpassungen).

| Maßnahme | Details |
|---|---|
| **Apache Tika 1.18 → 2.9.x** | Parser-API vollständig überarbeitet |
| **Apache Lucene 8.10.0 → 9.12.x** | Re-Indexierung aller Daten erforderlich |

**Tika-Migration (1.x → 2.x):**
- `tika-parsers` → `tika-parsers-standard-package` (neues Artefakt)
- `Tika.parseToString()` funktioniert weiterhin
- Für erweiterte Nutzung: `AutoDetectParser` API geändert
- Alle Tika-Abhängigkeiten müssen versionskonsistent sein

**Lucene-Migration (8 → 9):**
- Das Artefakt `lucene-analyzers-common` existiert nicht mehr; ist in `lucene-analysis-common` integriert:
```xml
<!-- ALT: -->
<artifactId>lucene-analyzers-common</artifactId>

<!-- NEU: -->
<artifactId>lucene-analysis-common</artifactId>
```
- Alle bestehenden Lucene-Indizes müssen nach dem Upgrade **vollständig neu aufgebaut** werden
- Indizes aus Lucene 8 können von Lucene 9 noch gelesen werden (für Migration), danach nicht mehr rückwärtskompatibel
- `IndexWriter` API: `SortedNumericDocValuesField` Verhalten geändert
- Upgrade von 9 auf 10 später möglich (erfordert dann Java 21)

---

### Phase 3 – Jakarta EE Migration (Kern-Migration, ~4–6 Wochen)

**Ziel:** Vollständige Migration auf Jakarta EE, Spring 6, Hibernate 6, Tomcat 10.  
**Java-Version:** 17 (bleibt)

Dies ist der komplexeste Teil und sollte in einem dedizierten Branch durchgeführt werden.

#### 3.1 Namensraum-Migration im Quellcode

Der Schritt betrifft **alle Java-Dateien und XML-Konfigurationen** im Projekt:

```bash
# Vereinfachtes Beispiel – in der Praxis: OpenRewrite oder IntelliJ-Migration nutzen!
find . -name "*.java" -exec sed -i \
  's/import javax\.servlet\./import jakarta.servlet./g;
   s/import javax\.persistence\./import jakarta.persistence./g;
   s/import javax\.validation\./import jakarta.validation./g;
   s/import javax\.annotation\./import jakarta.annotation./g;
   s/import javax\.inject\./import jakarta.inject./g;
   s/import javax\.mail\./import jakarta.mail./g;
   s/import javax\.ws\.rs\./import jakarta.ws.rs./g;
   s/import javax\.transaction\./import jakarta.transaction./g' {} \;
```

**Empfehlung: OpenRewrite verwenden** (automatisierte Migration):
```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.x</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.jakarta.JakartaEE10</recipe>
        </activeRecipes>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-migrate-java</artifactId>
            <version>2.x</version>
        </dependency>
    </dependencies>
</plugin>
```

#### 3.2 Spring Framework 4.3 → 6.2.x

```xml
<spring.version>6.2.18</spring.version>
```

**Breaking Changes und Maßnahmen:**
- Alle `@RequestMapping`-Handler-Methoden benötigen explizite HTTP-Method-Annotationen oder `@RequestMapping` mit `method`-Attribut (war in 5.x deprecated)
- `SimpleJdbcTemplate` und andere legacy APIs entfernt → `JdbcTemplate` verwenden
- Spring MVC: `DefaultAnnotationHandlerMapping` entfernt → `RequestMappingHandlerMapping`
- `WebMvcConfigurerAdapter` entfernt → `WebMvcConfigurer` direkt implementieren
- `AsyncConfigurer`-Methoden haben Standardimplementierungen (nicht mehr `null` zurückgeben)
- Thymeleaf/FreeMarker Integrationen: Versionen müssen abgestimmt werden

#### 3.3 Spring Security 4.2 → 6.5.x

```xml
<springSecurity.version>6.5.x</springSecurity.version>
```

**Breaking Changes:**
- `WebSecurityConfigurerAdapter` vollständig entfernt → auf komponentenbasierte Konfiguration umstellen:
```java
// ALT:
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) { ... }
}

// NEU:
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.build();
    }
}
```

#### 3.4 Spring Data 1.x → 3.5.x

```xml
<springDataJpa.version>3.5.x</springDataJpa.version>
<springDataEnvers.version>3.5.x</springDataEnvers.version>
<springData.version>3.5.x</springData.version>
```

**Breaking Changes:**
- `CrudRepository.findById()` gibt `Optional<T>` zurück (war `T`)
- `Page<T>` und `Pageable` API-Änderungen
- `@EnableJpaRepositories` Konfiguration anpassen

#### 3.5 Spring WS 2.4 → 4.0.x

```xml
<springWS.version>4.0.x</springWS.version>
```

#### 3.6 Hibernate ORM 5.4 → 6.6.x

```xml
<!-- groupId wechselt! -->
<!-- ALT: -->
<groupId>org.hibernate</groupId>
<artifactId>hibernate-core</artifactId>
<version>5.4.32.Final</version>

<!-- NEU: -->
<groupId>org.hibernate.orm</groupId>
<artifactId>hibernate-core</artifactId>
<version>6.6.x.Final</version>
```

**Breaking Changes:**
- **Legacy Hibernate Criteria API entfernt** — alle `session.createCriteria()` Aufrufe müssen auf JPA Criteria API oder QueryDSL umgestellt werden
- Neue Identifier-Generator-Strategie: Eine Sequence pro Entity-Hierarchie (statt globale `hibernate_sequence`) — kann zu Schema-Änderungen führen!
- Sequence-Inkrement-Default: 1 → 50 (verhindert häufige DB-Roundtrips, kann aber Lücken erzeugen)
- `NativeQuery` für Stored Procedures entfernt → `StoredProcedureQuery`
- `@Type` Annotation vollständig überarbeitet
- `hbm.xml` Mappings: Unterstützung stark reduziert, auf Annotations migrieren
- `persistence.xml` XSD-Namespace muss auf Jakarta aktualisiert werden:
```xml
<!-- ALT: -->
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
             http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">

<!-- NEU: -->
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
             https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
```

#### 3.7 Hibernate Validator 6.2 → 8.0.x

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.x.Final</version>
</dependency>
```

Erfordert `jakarta.validation-api` 3.0+.

#### 3.8 Tomcat 9.x → 10.1.x

```xml
<tomcat.version>10.1.55</tomcat.version>
```

Tomcat 10+ läuft nur mit vollständig auf `jakarta.*` migriertem Code.
Die WAR-Dateien werden mit `jakarta.servlet-api` statt `javax.servlet-api` kompiliert.

#### 3.9 Jakarta Mail: com.sun.mail → Eclipse Angus

```xml
<!-- ALT: -->
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>1.6.7</version>
</dependency>

<!-- NEU: API + Implementierung getrennt -->
<dependency>
    <groupId>jakarta.mail</groupId>
    <artifactId>jakarta.mail-api</artifactId>
    <version>2.1.3</version>
</dependency>
<dependency>
    <groupId>org.eclipse.angus</groupId>
    <artifactId>angus-mail</artifactId>
    <version>2.0.4</version>
    <scope>runtime</scope>
</dependency>
```

Code-Änderung: `import javax.mail.*` → `import jakarta.mail.*`

---

### Phase 4 – Camunda-Update (~1–2 Wochen)

**Ziel:** Camunda 7.15 → 7.24 (letzte CE-Version).

```xml
<camunda.version>7.24.0</camunda.version>
```

Camunda 7.x ist innerhalb der Major-Version weitgehend kompatibel.
Der Sprung von 7.15 auf 7.24 umfasst interne Verbesserungen ohne fundamentale API-Brüche.

**Langfristig:** Camunda 7 CE wird keine weiteren Releases erhalten. Eine Migration auf
**Camunda 8** (völlig neue Architektur, cloud-native, BPMN 2.0 Engine) oder eine
OSS-Alternative (z.B. Flowable, Activiti) sollte mittelfristig evaluiert werden.

---

### Phase 5 – Upgrade auf Spring 7 / Java 21 (optional, Zukunft)

**Voraussetzung:** Phase 0–3 abgeschlossen.

| Abhängigkeit | Von | Nach |
|---|---|---|
| Java | 17 | **21** |
| Spring Framework | 6.2.x | **7.0.7** |
| Spring Data | 3.5.x | **4.0.x** |
| Spring Security | 6.5.x | **7.0.x** |
| Hibernate ORM | 6.6.x | **7.4.x** |
| Lucene | 9.12.x | **10.4.x** (Java 21 Pflicht) |
| Jackson | 2.19.x | **3.x** (groupId-Wechsel!) |

**Jackson 3 Breaking Change:**
```xml
<!-- ALT (Jackson 2): -->
<groupId>com.fasterxml.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>

<!-- NEU (Jackson 3): -->
<groupId>tools.jackson.core</groupId>
<artifactId>jackson-databind</artifactId>
```

---

## Besondere Herausforderungen

### 1. JAXB und Codegenerierung

Das Projekt nutzt `jaxb2-maven-plugin` 1.6 zur Codegenerierung aus XSD-Dateien.

```xml
<!-- Aktuell: -->
<groupId>org.codehaus.mojo</groupId>
<artifactId>jaxb2-maven-plugin</artifactId>
<version>1.6</version>

<!-- Neu: -->
<groupId>org.codehaus.mojo</groupId>
<artifactId>jaxb2-maven-plugin</artifactId>
<version>3.2.0</version>
```

Ab Java 11 ist JAXB nicht mehr im JDK enthalten. Explizite Abhängigkeiten hinzufügen:
```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.2</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.5</version>
    <scope>runtime</scope>
</dependency>
```

### 2. QueryDSL APT-Codegenerierung

QueryDSL generiert zur Build-Zeit `Q*`-Klassen. Mit Hibernate 6 und Jakarta ändert sich
das APT-Setup:

```xml
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
    <version>5.1.0</version>
    <classifier>jakarta</classifier>  <!-- Neu erforderlich ab QueryDSL 5.1 mit Hibernate 6 -->
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
    <version>5.1.0</version>
    <classifier>jakarta</classifier>
    <scope>provided</scope>
</dependency>
```

### 3. Sonatype Nexus Staging Plugin

Der `nexus-staging-maven-plugin` ist für das alte OSSRH-System. Sonatype hat auf ein
neues Portal umgestellt:

```xml
<!-- ALT: -->
<groupId>org.sonatype.plugins</groupId>
<artifactId>nexus-staging-maven-plugin</artifactId>
<version>1.6.7</version>

<!-- NEU: -->
<groupId>org.sonatype.central</groupId>
<artifactId>central-publishing-maven-plugin</artifactId>
<version>0.7.0</version>
```

### 4. Ehcache

`net.sf.ehcache:ehcache-core:2.6.11` ist stark veraltet. Da Hazelcast bereits im
Projekt vorhanden ist, prüfen ob Ehcache noch aktiv genutzt wird und ggf. vollständig
durch Hazelcast ersetzen.

### 5. JodaTime

`joda-time:joda-time:2.10.11` — Mit Java 8+ gibt es die `java.time.*`-API (JSR-310)
als nativen Ersatz. JodaTime selbst empfiehlt die Migration. Mit Spring 6 und
Hibernate 6 ist `java.time` vollständig integriert und JodaTime kann entfernt werden.

### 6. commons-fileupload

`commons-fileupload:1.5` ist abgekündigt. Ab Spring 6 / Servlet 5+ ist Multipart-Handling
nativ in der Servlet-API und Spring MVC integriert. `CommonsMultipartResolver` entfernt
in Spring 6 → `StandardServletMultipartResolver` verwenden.

### 7. Apache HttpComponents

`httpcore:4.4.14` und `httpclient:4.5.13` sind die alten 4.x-Versionen. Die aktuelle
Version ist HttpClient 5.x mit grundlegend neuer API. Spring 6 bevorzugt HttpClient 5.x.

---

## Empfohlene Tooling-Unterstützung

### OpenRewrite (automatisierte Migrationen)

OpenRewrite bietet fertige Rezepte für die wichtigsten Migrationen:

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.x</version>
    <configuration>
        <activeRecipes>
            <!-- Jakarta EE 10 Namespace-Migration -->
            <recipe>org.openrewrite.java.migrate.jakarta.JakartaEE10</recipe>
            <!-- Spring Boot 3 Migration (enthält Spring 6) -->
            <recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0</recipe>
            <!-- JUnit 4 → 5 -->
            <recipe>org.openrewrite.java.testing.junit5.JUnit4to5Migration</recipe>
            <!-- Mockito 1/2 → 5 -->
            <recipe>org.openrewrite.java.testing.mockito.Mockito1to5Migration</recipe>
            <!-- BouncyCastle jdk15on → jdk18on -->
            <recipe>org.openrewrite.java.migrate.BounceCastleFromJdk15OnToJdk18On</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

### IntelliJ IDEA Migration

IntelliJ bietet unter `Refactor → Migrate → Java EE to Jakarta EE` eine automatische
Namespace-Migration für den gesamten Quellcode.

---

## Risikobewertung

| Risiko | Wahrscheinlichkeit | Auswirkung | Minderung |
|---|---|---|---|
| Hibernate Criteria API Nutzung | Hoch | Hoch | Vorher Codebase scannen |
| Unerwartete javax-Importe | Mittel | Mittel | OpenRewrite/IntelliJ Migration |
| Lucene Index-Inkompatibilität | Sehr hoch | Hoch | Re-Indexierung einplanen |
| Spring Security Config-Umbau | Hoch | Hoch | Frühzeitig testen |
| Flyway-Migration fehlschlägt | Mittel | Sehr hoch | Backup + Staging-Umgebung |
| Camunda 7 → Jakarta Konflikt | Hoch | Hoch | Camunda 7.24 vor Jakarta-Step |
| QueryDSL Q-Klassen-Regenerierung | Hoch | Mittel | Clean Build nach Upgrade |
| JAXB-Codegenerierung bricht | Mittel | Mittel | Plugin-Version prüfen |

---

## Gesamtzeitplan (Schätzung)

| Phase | Beschreibung | Dauer |
|---|---|---|
| **Phase 0** | Sofortmaßnahmen (Security, Trivial-Updates) | 1 Woche |
| **Phase 1** | Java 17 + einfache Upgrades | 2 Wochen |
| **Phase 2** | Tika + Lucene 9 | 2 Wochen |
| **Phase 3** | Jakarta EE Migration (Spring 6, Hibernate 6, Tomcat 10) | 4–6 Wochen |
| **Phase 4** | Camunda 7.15 → 7.24 | 1–2 Wochen |
| **Phase 5** | Spring 7 / Java 21 (optional) | 2–4 Wochen |
| **Gesamt** | | **~12–17 Wochen** |

---

## Voraussetzungen für den Start

1. **Testabdeckung prüfen:** Vor jedem Upgrade-Schritt sicherstellen, dass ausreichend
   Integrationstests vorhanden sind. Fehlende Tests jetzt schreiben.
2. **Dedizierter Branch:** Alle Migrations-Arbeiten in einem separaten Git-Branch.
3. **Staging-Umgebung:** Alle Phasen zuerst in einer Staging-Umgebung testen,
   insbesondere Flyway-Migrationen und Lucene-Reindexierung.
4. **Datenbank-Backup:** Vor Phase 3 vollständiges DB-Backup wegen Hibernate-Identifier-Änderungen.
5. **CI/CD anpassen:** Jenkins-Job auf Java 17 umstellen.
