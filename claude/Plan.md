# appNG – Migrationsplan: Modernisierung des Tech-Stacks

> Stand: Juni 2026 · Basis: appNG 1.26.6-SNAPSHOT  
> Quellen: [Analyse.md](Analyse.md) · [Upgrade-Plan.md](Upgrade-Plan.md)

---

## Zielbild

| Dimension | Aktuell | Ziel |
|---|---|---|
| Java | 8 | **21** |
| Tomcat | 9.0.31 | **11.0.22** (Jakarta EE 11) |
| Spring Framework | 4.3.30.RELEASE | **7.0.7** |
| Hibernate ORM | 5.4.32.Final | **7.4.x.Final** |
| Namespace | `javax.*` | **`jakarta.*`** |

Tomcat 11 setzt Jakarta EE 11 (Servlet 6.1) voraus. Spring 7 ist der erste Release, der
Jakarta EE 11 vollständig unterstützt und erfordert Java 21. Damit ergibt sich eine
konsistente, zukunftssichere Zielkonfiguration ohne Mischzustände.

---

## Zielversionen aller Abhängigkeiten

### Framework & Laufzeit

| Property / Artefakt | Alt | Neu |
|---|---|---|
| `spring.version` | 4.3.30.RELEASE | **7.0.7** |
| `springDataJpa.version` | 1.11.23.RELEASE | **4.0.5** |
| `springDataEnvers.version` | 1.1.23.RELEASE | **4.0.5** |
| `springData.version` | 1.13.23.RELEASE | **4.0.5** |
| `springSecurity.version` | 4.2.17.RELEASE | **7.0.4** |
| `springWS.version` | 2.4.6.RELEASE | **4.0.x** |
| `hibernate.version` | 5.4.32.Final | **7.4.x.Final** |
| Hibernate Validator | 6.2.0.Final | **9.1.0.Final** |
| `tomcat.version` | 9.0.31 | **11.0.22** |
| `servlet.version` | 3.1.0 (`javax.servlet-api`) | **6.1.0** (`jakarta.servlet-api`) |
| JSP API | `javax.servlet.jsp:jsp-api:2.2` | **`jakarta.servlet.jsp:jakarta.servlet.jsp-api:4.0.0`** |

### Suche & Dokumentenverarbeitung

| Property | Alt | Neu |
|---|---|---|
| `lucene.version` | 8.10.0 | **10.4.0** |
| `tika.version` | 1.18 | **3.3.1** |
| `pdfBox.version` | 2.0.24 | **3.0.7** |
| `poi.version` | 3.17 | **5.5.1** |

### Datenbank & Persistenz

| Property / Artefakt | Alt | Neu |
|---|---|---|
| `flyway.version` | 8.5.13 | **10.x** |
| `queryDsl.version` | 5.0.0 | **5.1.0** + Classifier `jakarta` |
| HikariCP | 4.0.3 | **6.x** |
| MySQL Connector | `mysql:mysql-connector-java:8.0.28` | **`com.mysql:mysql-connector-j:9.x`** |
| HSQLDB | 2.5.0 | **2.7.4** |
| TestContainers | 1.16.3 | **1.21.x** |
| DBUnit | 2.6.0 | **2.8.0** |

### Logging & Security

| Property / Artefakt | Alt | Neu |
|---|---|---|
| `slf4j.version` | 1.7.26 | **2.0.18** |
| Logback | — | **1.5.34** (neu hinzufügen) |
| `log4j:log4j:1.2.17` | ⚠️ **ENTFERNEN** | → Logback |
| `slf4j-log4j12` | **ENTFERNEN** | → `logback-classic` |
| `bouncycastle.version` | 1.69 (`jdk15on`) | **1.84** (`jdk18on`) |
| `esapi.version` | 2.2.3.1 | **2.7.0.0** |

### Jakarta-Namespace-Artefakte

| Artefakt | Alt | Neu |
|---|---|---|
| JPA API | `jakarta.persistence-api:2.2.3` | **`jakarta.persistence-api:3.2.0`** |
| Bean Validation | `jakarta.validation-api:2.0.2` | **`jakarta.validation-api:3.1.1`** |
| Annotation API | `jakarta.annotation-api:1.3.5` | **`jakarta.annotation-api:3.0.0`** |
| Inject API | `jakarta.inject-api:1.0.3` | **`jakarta.inject-api:2.0.1`** |
| Activation API | `jakarta.activation-api:1.2.2` | **`jakarta.activation-api:2.1.3`** |
| WS-RS API | `jakarta.ws.rs-api:2.1.6` | **`jakarta.ws.rs-api:4.0.0`** |
| Mail | `com.sun.mail:jakarta.mail:1.6.7` | **`jakarta.mail:jakarta.mail-api:2.1.3`** + `org.eclipse.angus:angus-mail:2.0.4` |
| JAXB API | `javax.xml.bind:jaxb-api:2.3.1` | **`jakarta.xml.bind:jakarta.xml.bind-api:4.0.2`** |
| JAXB Runtime | — | **`org.glassfish.jaxb:jaxb-runtime:4.0.5`** (runtime scope) |
| JAX-WS | `com.sun.xml.ws:jaxws-rt:2.3.5` | **`com.sun.xml.ws:jaxws-rt:4.0.x`** |
| javax.cache | `javax.cache:cache-api:1.1.1` | **`javax.cache:cache-api:1.1.1`** *(JSR-107 bleibt javax)* |

### Weitere Libraries

| Artefakt | Alt | Neu |
|---|---|---|
| `jackson.version` | 2.14.1 | **2.19.x** |
| `hazelcast.version` | 5.3.0 | **5.6.0** |
| `camunda.version` | 7.15.0 | **7.24.0** |
| Lombok | 1.16.22 | **1.18.46** |
| JCommander | 1.72 | **1.82** |
| Quartz | 2.3.2 | **2.5.2** |
| Saxon-HE | 10.6 | **12.x** |
| Freemarker | 2.3.31 | **2.3.33** |
| Guava | 31.0.1-jre | **33.x** |
| JSoup | 1.15.3 | **1.22.2** |
| JUnit | 4.13.2 (`junit:junit`) | **5.14.4** (`org.junit.jupiter:junit-jupiter`) |
| Mockito | 1.10.19 (`mockito-all`) | **5.23.0** (`mockito-core`) |
| xmlunit | 1.6 | **2.10.x** (`org.xmlunit:xmlunit-core`) |
| Spring LDAP | 2.3.2.RELEASE | **3.2.x** |
| Thymeleaf | `thymeleaf-spring4:3.0.12` | **`thymeleaf-spring6:3.1.x`** |
| Keycloak | 15.0.2 | **25.x** (Breaking Changes, separate Planung) |
| Jedis (Redis) | 3.7.0 | **5.x** |
| RabbitMQ Client | 5.13.1 | **5.24.x** |
| Prometheus | `simpleclient_common:0.16.0` | **`io.micrometer:micrometer-registry-prometheus`** |

### Maven-Plugins

| Plugin | Alt | Neu |
|---|---|---|
| `maven-compiler-plugin` | 3.6.1 (`source/target 1.8`) | **3.13.0** (`release 21`) |
| `jaxb2-maven-plugin` | 1.6 (root) / 2.4 (appng-appngizer-jaxb) | **3.2.0** (beide) |
| `maven-war-plugin` | 3.1.0 | **3.4.0** |
| `maven-jar-plugin` | 3.0.2 | **3.4.2** |
| `maven-javadoc-plugin` | 2.10.4 | **3.11.2** |
| `maven-surefire-plugin` | 3.0.0-M5 | **3.5.3** |
| `maven-resources-plugin` | 3.0.2 | **3.3.1** |
| `maven-assembly-plugin` | 3.0.0 | **3.7.1** |
| `maven-dependency-plugin` | 3.0.1 | **3.8.1** |
| `asciidoctor-maven-plugin` | 1.5.6 | **3.1.0** |
| `jacoco-maven-plugin` | 0.8.5 | **0.8.13** |
| `sonar-maven-plugin` | 3.7.0.1746 | **5.0.0.4389** |
| `nexus-staging-maven-plugin` | 1.6.7 | **`central-publishing-maven-plugin:0.7.0`** (Sonatype-Umstellung) |
| `build-helper-maven-plugin` | 3.0.0 | **3.6.0** |

---

## Phase 0 – Sofortmaßnahmen (Sicherheit, ohne Breaking Changes)

**Dauer: ~1 Woche · Java-Version: bleibt 8**

### 0.1 Log4j 1.x entfernen — KRITISCH

Log4j 1.2.17 hat mehrere ungepatchte CVEs (u.a. CVE-2019-17571). Betroffen sind:
- `appng-xmlapi/pom.xml` (`slf4j-log4j12`)
- `appng-appngizer-jaxb/pom.xml` (`slf4j-log4j12`)
- `appng-persistence/pom.xml` (`slf4j-log4j12`)
- `appng-core/pom.xml` (`log4j:log4j` direkt + `slf4j-log4j12`)
- `appng-application/pom.xml` (`log4j:log4j` direkt)
- `appng-forms/pom.xml` (`slf4j-log4j12`)

In der Root-POM `pom.xml` ersetzen:
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
    <version>${slf4j.version}</version>
</dependency>

<!-- HINZUFÜGEN: -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.13</version>  <!-- SLF4J 1.7 kompatibel für Phase 0 -->
</dependency>
```

Alle `log4j.properties`-Dateien durch `logback.xml` ersetzen.

### 0.2 Mockito bereinigen

In allen Modul-POMs `mockito-all` durch `mockito-core` ersetzen:
```xml
<!-- ENTFERNEN: mockito-all:1.10.19 -->
<!-- HINZUFÜGEN: -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.11.0</version>  <!-- Java 8 kompatibel für Phase 0 -->
    <scope>test</scope>
</dependency>
```

Betroffen: `appng-forms`, `appng-api`, `appng-core` (alle direkt oder via testsupport).

### 0.3 Triviale Patch-Updates (keine Breaking Changes)

```xml
<!-- root pom.xml properties: -->
<hazelcast.version>5.6.0</hazelcast.version>
<esapi.version>2.7.0.0</esapi.version>
<jsoup>1.22.2</jsoup>  <!-- in dependencyManagement -->
```

Lombok in root-POM:
```xml
<version>1.18.46</version>
```

Tomcat-Sicherheitspatch (noch kein Namespace-Wechsel):
```xml
<tomcat.version>9.0.118</tomcat.version>
```

---

## Phase 1 – Java 17 + Infrastruktur-Upgrades

**Dauer: ~2 Wochen · Java-Version: 8 → 17**

### 1.1 Compiler auf Java 17 umstellen

In root `pom.xml`:
```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <release>17</release>
        <encoding>${project.build.sourceEncoding}</encoding>
    </configuration>
</plugin>
```

`argLine` anpassen (JVM-Args für Java 17):
```xml
<argLine>-Xms512m -Xmx1g -XX:MaxMetaspaceSize=256m
         -Dfile.encoding=${project.build.sourceEncoding}
         --add-opens java.base/java.lang=ALL-UNNAMED</argLine>
```

### 1.2 SLF4J 2.x + Logback 1.5

```xml
<slf4j.version>2.0.18</slf4j.version>
```

Logback updaten (SLF4J 2.x erfordert Logback 1.3+):
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.34</version>
</dependency>
```

SLF4J 2.x verwendet ServiceLoader statt `StaticLoggerBinder` — `logback.xml` Konfiguration
bleibt kompatibel, keine Code-Änderungen erforderlich.

### 1.3 MySQL Connector (groupId-Wechsel)

```xml
<!-- ALT in root pom.xml dependencyManagement: -->
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<version>8.0.28</version>

<!-- NEU: -->
<groupId>com.mysql</groupId>
<artifactId>mysql-connector-j</artifactId>
<version>9.3.0</version>
```

Alle Modul-POMs prüfen (betroffen: `appng-core` direkt referenziert).

### 1.4 Flyway 10.x

```xml
<flyway.version>10.x</flyway.version>
```

`cleanDisabled` ist ab Flyway 9 standardmäßig `true` — in Testumgebungen explizit aktivieren:
```properties
flyway.cleanDisabled=false
```

Datenbank-spezifische Artefakte bereits vorhanden (`flyway-mysql`, `flyway-sqlserver`) —
Versionen anpassen, keine strukturellen Änderungen nötig.

### 1.5 Apache POI 3.17 → 5.5.1

```xml
<poi.version>5.5.1</poi.version>
```

POI 4.x+: Java 11 Minimum. API-Änderungen:
- `HSSFWorkbook`/`XSSFWorkbook`: weitgehend kompatibel
- `WorkbookFactory.create()` empfohlen statt direkter Klassen-Instanzierung
- interne Paketstruktur geändert — Importe prüfen

### 1.6 Apache PDFBox 3.0.7

```xml
<pdfBox.version>3.0.7</pdfBox.version>
```

### 1.7 Weitere kleine Updates

```xml
<jackson.version>2.19.x</jackson.version>
<queryDsl.version>5.1.0</queryDsl.version>
<!-- HikariCP (in dependencyManagement): -->
<version>6.2.1</version>
<!-- Quartz: -->
<version>2.5.2</version>
<!-- Saxon-HE: -->
<version>12.5</version>
<!-- Freemarker: -->
<version>2.3.33</version>
<!-- Guava: -->
<version>33.4.8-jre</version>
<!-- HSQLDB: -->
<version>2.7.4</version>
<!-- DBUnit: -->
<version>2.8.0</version>
<!-- TestContainers: -->
<testcontainers.version>1.21.1</testcontainers.version>
```

---

## Phase 2 – JAXB-Migration (Startpunkt: appng-xmlapi)

**Dauer: ~1–2 Wochen · Java-Version: 17**

JAXB ist nicht mehr Bestandteil des JDK (seit Java 11). Drei Module generieren Code aus
XSD-Schemata per `jaxb2-maven-plugin`. Da `appng-xmlapi` die Basis für `appng-api`,
`appng-core` und alle weiteren Module ist, wird es **zuerst** migriert.

### JAXB-Module und ihre XSDs

| Modul | Plugin-Version | XSD-Dateien | Generiertes Package |
|---|---|---|---|
| `appng-xmlapi` | 1.6 (von root) | appng-platform.xsd, appng-application.xsd | `org.appng.xml.platform`, `org.appng.xml.application` |
| `appng-appngizer-jaxb` | 2.4 (explizit) | appngizer.xsd | `org.appng.appngizer.model.xml` |
| `appng-core` | 1.6 (von root) | appng-repository.xsd | `org.appng.core.xml.repository` |

### 2.1 Root-POM: jaxb2-maven-plugin und JAXB-Runtime

In `pom.xml` `pluginManagement` aktualisieren:
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jaxb2-maven-plugin</artifactId>
    <version>3.2.0</version>
</plugin>
```

In `dependencyManagement` hinzufügen:
```xml
<!-- JAXB API (Jakarta) -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.2</version>
</dependency>
<!-- JAXB Runtime (für Code-Generierung und Laufzeit) -->
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.5</version>
    <scope>runtime</scope>
</dependency>
```

Alte JAXB-Artefakte aus `dependencyManagement` entfernen:
```xml
<!-- ENTFERNEN: javax.xml.bind:jaxb-api -->
```

### 2.2 appng-xmlapi/pom.xml

Plugin-Konfiguration auf Jakarta JAXB 4.0 umstellen:
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jaxb2-maven-plugin</artifactId>
    <!-- Version 3.2.0 aus pluginManagement -->
    <executions>
        <execution>
            <id>generate-xmlapi</id>
            <goals><goal>xjc</goal></goals>
            <phase>generate-sources</phase>
            <configuration>
                <!-- target entfällt in v3.x, stattdessen: -->
                <packageName>org.appng.xml.platform</packageName>
                <bindingFiles>bindings.xml</bindingFiles>
                <schemaFiles>appng-platform.xsd</schemaFiles>
                <extension>true</extension>
                <clearOutputDir>false</clearOutputDir>
            </configuration>
        </execution>
        <execution>
            <id>generate-plugininfo</id>
            <goals><goal>xjc</goal></goals>
            <configuration>
                <packageName>org.appng.xml.application</packageName>
                <schemaFiles>appng-application.xsd</schemaFiles>
                <extension>true</extension>
                <clearOutputDir>false</clearOutputDir>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Abhängigkeiten hinzufügen:
```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
</dependency>
```

`slf4j-log4j12` entfernen (Phase 0 erledigt).

**Erwartete Auswirkung:** Die generierten Klassen unter `target/generated-sources/jaxb`
enthalten nun `jakarta.xml.bind.*`-Annotationen statt `javax.xml.bind.*`. Alle
Module, die diese Klassen nutzen, müssen ggf. ebenfalls ihre JAXB-Imports anpassen
(das passiert aber automatisch durch die Codegenerierung; handgeschriebener Code, der
direkt JAXB-Klassen referenziert, muss manuell angepasst werden).

### 2.3 appng-appngizer-jaxb/pom.xml

Explizite Plugin-Version von 2.4 auf 3.2.0 anheben:
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jaxb2-maven-plugin</artifactId>
    <version>3.2.0</version>  <!-- war: 2.4 -->
    <executions>
        <execution>
            <id>generate-xmlapi</id>
            <goals><goal>xjc</goal></goals>
            <phase>generate-sources</phase>
            <configuration>
                <!-- target entfernen, konfiguration vereinfachen -->
                <packageName>org.appng.appngizer.model.xml</packageName>
                <sources>
                    <source>src/main/resources/appngizer.xsd</source>
                </sources>
                <bindingFiles>bindings.xml</bindingFiles>
                <extension>true</extension>
                <clearOutputDir>false</clearOutputDir>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Außerdem: den obsoleten Compiler-Hack entfernen (`-XDignore.symbol.file`, `<fork>true</fork>`
— war ein Workaround für interne JDK-APIs, die mit Java 17 sowieso gesperrt sind).

### 2.4 appng-core/pom.xml (JAXB-Teil)

Die JAXB-Exclusions in der Hibernate-Dependency anpassen:
```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>  <!-- groupId wechselt in Phase 3 -->
    <artifactId>hibernate-core</artifactId>
    <exclusions>
        <exclusion>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
        </exclusion>
        <!-- jaxb-runtime nicht mehr ausschließen — wird nun als Runtime-Dep gebraucht -->
    </exclusions>
</dependency>
```

### 2.5 appng-api/pom.xml (appng-maven-plugin JAXB-Dep)

Das `appng-maven-plugin` hat eine explizite Plugin-Dependency auf `javax.xml.bind:jaxb-api`:
```xml
<plugin>
    <groupId>org.appng</groupId>
    <artifactId>appng-maven-plugin</artifactId>
    <version>1.24.5</version>
    <dependencies>
        <!-- ALT: -->
        <!-- <groupId>javax.xml.bind</groupId><artifactId>jaxb-api</artifactId><version>2.3.1</version> -->
        <!-- NEU: -->
        <dependency>
            <groupId>jakarta.xml.bind</groupId>
            <artifactId>jakarta.xml.bind-api</artifactId>
            <version>4.0.2</version>
        </dependency>
    </dependencies>
</plugin>
```

Achtung: Das `appng-maven-plugin` selbst (1.24.5) muss ggf. ebenfalls aktualisiert oder
angepasst werden, falls es intern javax-Klassen nutzt.

### 2.6 Handgeschriebener JAXB-Code

Alle `.java`-Dateien nach direkten JAXB-Importen durchsuchen:
```bash
grep -r "javax.xml.bind" --include="*.java" src/main/java src/test/java
```

Gefundene Imports umbenennen:
- `javax.xml.bind.*` → `jakarta.xml.bind.*`
- `javax.xml.bind.annotation.*` → `jakarta.xml.bind.annotation.*`

---

## Phase 3 – Lucene & Tika

**Dauer: ~2 Wochen · Java-Version: 17**

### 3.1 Apache Tika 1.18 → 3.3.1

```xml
<tika.version>3.3.1</tika.version>
```

**Kritische Änderungen (1.x → 3.x):**
- `tika-parsers` → **`tika-parsers-standard-package`**
- `AutoDetectParser`-Konstruktor geändert
- `Parser.parse(InputStream, ContentHandler, Metadata, ParseContext)` — Interface bleibt stabil
- `Tika.parseToString()` bleibt kompatibel (Facade-API)

In `appng-search/pom.xml` Artefakt umbenennen:
```xml
<!-- ALT: -->
<artifactId>tika-parsers</artifactId>
<!-- NEU: -->
<artifactId>tika-parsers-standard-package</artifactId>
```

### 3.2 Apache Lucene 8.10.0 → 10.4.0

```xml
<lucene.version>10.4.0</lucene.version>
```

**Lucene 10 erfordert Java 21** — dieser Schritt kann erst in Phase 5 (Java 21) final
abgeschlossen werden. Übergangsweise Lucene 9.12.x für Java 17 nutzen:
```xml
<lucene.version>9.12.3</lucene.version>  <!-- Phase 3, Java 17 -->
<!-- → 10.4.0 in Phase 5 nach Java-21-Umstieg -->
```

**Kritische Änderung: Artefakt-Umbenennung** (betrifft `appng-search/pom.xml`):
```xml
<!-- ALT: -->
<artifactId>lucene-analyzers-common</artifactId>
<!-- NEU: -->
<artifactId>lucene-analysis-common</artifactId>
```

**Re-Indexierung:** Lucene-Indizes aus Version 8 können von Version 9 noch gelesen werden,
aber das Format muss nach dem Upgrade vollständig neu aufgebaut werden. Ein einmaliger
Re-Index-Lauf ist nach dem Deployment zwingend erforderlich.

Code-Änderungen in `appng-search`:
- `OrdinalsReader` und Unterklassen entfernt → prüfen ob genutzt
- `TopDocsCollector` wirft `IllegalArgumentException` bei 0 als `numHits` (war vorher leer)
- Import-Pfade für einige interne Klassen geändert

---

## Phase 4 – Jakarta EE Migration (Kern-Migration)

**Dauer: ~4–6 Wochen · Java-Version: 17 → 21 am Ende**

Dies ist der komplexeste Schritt. Empfehlung: separater Git-Branch `feature/jakarta-migration`.

### 4.1 Automatische Namespace-Migration (Startpunkt)

IntelliJ IDEA: `Refactor → Migrate Packages and Classes → Java EE to Jakarta EE`

Alternativ OpenRewrite (in root `pom.xml` temporär einfügen):
```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.x</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.jakarta.JakartaEE10</recipe>
            <recipe>org.openrewrite.java.testing.junit5.JUnit4to5Migration</recipe>
            <recipe>org.openrewrite.java.testing.mockito.Mockito1to5Migration</recipe>
            <recipe>org.openrewrite.java.migrate.BounceCastleFromJdk15OnToJdk18On</recipe>
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

Namespace-Änderungen im Quellcode:

| Alt | Neu |
|---|---|
| `javax.servlet.*` | `jakarta.servlet.*` |
| `javax.persistence.*` | `jakarta.persistence.*` |
| `javax.validation.*` | `jakarta.validation.*` |
| `javax.annotation.*` | `jakarta.annotation.*` |
| `javax.inject.*` | `jakarta.inject.*` |
| `javax.mail.*` | `jakarta.mail.*` |
| `javax.ws.rs.*` | `jakarta.ws.rs.*` |
| `javax.transaction.*` | `jakarta.transaction.*` |

### 4.2 Root-POM: Servlet-API tauschen

```xml
<!-- ENTFERNEN: -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>${servlet.version}</version>
</dependency>
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.2</version>
</dependency>

<!-- HINZUFÜGEN: -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>jakarta.servlet.jsp</groupId>
    <artifactId>jakarta.servlet.jsp-api</artifactId>
    <version>4.0.0</version>
    <scope>provided</scope>
</dependency>
```

`servlet.version` Property entfernen oder umbenennen.

### 4.3 Hibernate ORM 5.4 → 7.4.x (groupId-Wechsel!)

```xml
<!-- ALT groupId: org.hibernate -->
<!-- NEU groupId: org.hibernate.orm -->
<hibernate.version>7.4.x.Final</hibernate.version>
```

In allen Modul-POMs `org.hibernate:hibernate-core` → `org.hibernate.orm:hibernate-core` umbenennen
(betroffen: `appng-persistence`, `appng-core`, `appng-testsupport`).

**Kritische Breaking Changes:**

1. **Legacy Criteria API entfernt** — alle `Session.createCriteria()` Aufrufe müssen
   auf JPA `CriteriaBuilder` oder QueryDSL umgestellt werden:
   ```bash
   grep -r "createCriteria\|Criteria\b" --include="*.java" src/
   ```

2. **Identifier-Generator-Strategie geändert** (kann Schema-Änderungen verursachen!):
   ```java
   // Explizit setzen um altes Verhalten zu erhalten:
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
   @SequenceGenerator(name = "seq", sequenceName = "hibernate_sequence", allocationSize = 1)
   ```

3. **`persistence.xml` XSD-Namespace** aktualisieren (→ siehe Phase 2.4 Muster).

4. **`@Type` Annotation** komplett überarbeitet — custom Type-Implementierungen müssen
   auf `@JdbcType`/`@JavaType` (Hibernate 6+ API) umgestellt werden.

5. **`hbm.xml`-Mappings** stark eingeschränkt — auf Annotationen migrieren.

### 4.4 QueryDSL 5.1.0 mit Jakarta-Classifier

```xml
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
    <version>5.1.0</version>
    <classifier>jakarta</classifier>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
    <version>5.1.0</version>
    <classifier>jakarta</classifier>
    <scope>provided</scope>
</dependency>
```

In `appng-persistence/pom.xml` die `javax.inject`-Exclusion entfernen (ist jetzt nativ jakarta).

Alle `Q*`-Klassen müssen nach dem Upgrade neu generiert werden (`mvn clean generate-sources`).

### 4.5 Spring Framework 4.3 → 7.0.7

```xml
<spring.version>7.0.7</spring.version>
```

**Breaking Changes (4.x → 7.x) — Auswahl der wichtigsten:**

- `WebMvcConfigurerAdapter` entfernt → `WebMvcConfigurer` Interface direkt implementieren
- `SimpleJdbcTemplate` entfernt → `JdbcTemplate`
- `DefaultAnnotationHandlerMapping` entfernt → `RequestMappingHandlerMapping`
- `spring-web` nutzt Jakarta Servlet API (kein `javax.*` mehr)
- `CommonsMultipartResolver` entfernt → `StandardServletMultipartResolver`

In `appng-core/pom.xml`: Thymeleaf Spring-Integration tauschen:
```xml
<!-- ALT: -->
<groupId>org.thymeleaf</groupId>
<artifactId>thymeleaf-spring4</artifactId>
<version>3.0.12.RELEASE</version>

<!-- NEU: -->
<groupId>org.thymeleaf</groupId>
<artifactId>thymeleaf-spring6</artifactId>
<version>3.1.x</version>
```

Spring Data aktualisieren:
```xml
<springDataJpa.version>4.0.5</springDataJpa.version>
<springDataEnvers.version>4.0.5</springDataEnvers.version>
<springData.version>4.0.5</springData.version>
```

**Spring Data Breaking Changes:**
- `CrudRepository.findById()` gibt `Optional<T>` zurück (immer schon, aber jetzt stricter)
- `Page.getTotalElements()` und ähnliche Methoden API-Stabilität prüfen
- `@EnableJpaRepositories` Konfiguration ggf. anpassen

### 4.6 Spring Security 4.2 → 7.0.4

```xml
<springSecurity.version>7.0.4</springSecurity.version>
```

`WebSecurityConfigurerAdapter` entfernt — Security-Konfiguration umschreiben:
```java
// ALT:
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override protected void configure(HttpSecurity http) { ... }
}

// NEU:
@Configuration
public class AppSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ... bisherige Konfiguration hierhin
        return http.build();
    }
}
```

### 4.7 Spring WS 2.4 → 4.0.x

```xml
<springWS.version>4.0.x</springWS.version>
```

### 4.8 Hibernate Validator 6.2 → 9.1.0

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>9.1.0.Final</version>
</dependency>
```

Erfordert `jakarta.validation-api:3.1.1`.

### 4.9 Jakarta Mail: com.sun.mail → Eclipse Angus

```xml
<!-- ENTFERNEN: com.sun.mail:jakarta.mail:1.6.7 -->
<!-- HINZUFÜGEN: -->
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

Im Code: `import javax.mail.*` → `import jakarta.mail.*` (von 4.1 bereits erledigt).

### 4.10 BouncyCastle Artifact-Rename

```xml
<!-- ÜBERALL ersetzen (3 Artefakte): -->
bcprov-jdk15on  →  bcprov-jdk18on
bcpkix-jdk15on  →  bcpkix-jdk18on
bcmail-jdk15on  →  bcmail-jdk18on

<bouncycastle.version>1.84</bouncycastle.version>
```

### 4.11 Tomcat 9.x → 11.0.22

```xml
<tomcat.version>11.0.22</tomcat.version>
```

Tomcat 11 liefert Servlet 6.1, JSP 4.0 und WebSocket 2.2.
Alle WARs (`appng-application`, `appng-application-camunda`, `appng-upngizr`) laufen
nach der Namespace-Migration aus 4.1 korrekt auf Tomcat 11.

### 4.12 JUnit 4 → 5 + Mockito 5

```xml
<!-- ENTFERNEN: junit:junit -->
<!-- HINZUFÜGEN: -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.14.4</version>
    <scope>test</scope>
</dependency>
<!-- Für Abwärtskompatibilität während der Migration: -->
<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <version>5.14.4</version>
    <scope>test</scope>
</dependency>

<!-- xmlunit: -->
<dependency>
    <groupId>org.xmlunit</groupId>
    <artifactId>xmlunit-core</artifactId>
    <version>2.10.x</version>
    <scope>test</scope>
</dependency>
```

Mockito finalisieren:
```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.23.0</version>
    <scope>test</scope>
</dependency>
```

### 4.13 Weitere Module-spezifische Änderungen in appng-core

| Abhängigkeit | Alt | Neu |
|---|---|---|
| Keycloak | `keycloak-core:15.0.2` | **25.x** — massive Breaking Changes, separate Planung empfohlen |
| Jedis | `3.7.0` | **5.x** (neue Connection-Pool-API) |
| RabbitMQ Client | `5.13.1` | **5.24.x** |
| Prometheus | `simpleclient_common:0.16.0` | **`io.micrometer:micrometer-registry-prometheus`** |
| Spring LDAP | `spring-ldap-ldif-core:2.3.2` | **`3.2.x`** |
| TestContainers | `1.16.3` | **`1.21.x`** |
| mssql-jdbc | `10.2.0.jre8` | **`12.x.x.jre11`** |

### 4.14 commons-fileupload entfernen

Ab Spring 6 / Servlet 5+ ist `CommonsMultipartResolver` entfernt.
Spring MVC nutzt `StandardServletMultipartResolver` nativ:

```java
// In Spring MVC Konfiguration:
@Bean
public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
}
```

`commons-fileupload` aus `pom.xml` entfernen falls nicht anderweitig genutzt.

### 4.15 JAX-WS aktualisieren (appng-core)

```xml
<!-- ALT: -->
<groupId>com.sun.xml.ws</groupId>
<artifactId>jaxws-rt</artifactId>
<version>2.3.5</version>

<!-- NEU: -->
<groupId>com.sun.xml.ws</groupId>
<artifactId>jaxws-rt</artifactId>
<version>4.0.3</version>
```

---

## Phase 5 – Java 21 + Lucene 10 + Finalisierung

**Dauer: ~1–2 Wochen**

### 5.1 Java 21 als Zielversion

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>21</release>
    </configuration>
</plugin>
```

### 5.2 Lucene 10.4.0 (Java 21 Pflicht)

```xml
<lucene.version>10.4.0</lucene.version>
```

Upgrade von Lucene 9 auf 10 erfordert erneute vollständige **Re-Indexierung**.
Interne APIs prüfen (insb. `OrdinalsReader`, `BinaryDocValues`-Nutzung).

### 5.3 Maven-Plugins finalisieren

```xml
<!-- pluginManagement in root pom.xml: -->
<maven-compiler-plugin>3.13.0</maven-compiler-plugin>
<maven-war-plugin>3.4.0</maven-war-plugin>
<maven-jar-plugin>3.4.2</maven-jar-plugin>
<maven-javadoc-plugin>3.11.2</maven-javadoc-plugin>
<maven-surefire-plugin>3.5.3</maven-surefire-plugin>
<maven-resources-plugin>3.3.1</maven-resources-plugin>
<maven-assembly-plugin>3.7.1</maven-assembly-plugin>
<maven-dependency-plugin>3.8.1</maven-dependency-plugin>
<build-helper-maven-plugin>3.6.0</build-helper-maven-plugin>
<asciidoctor-maven-plugin>3.1.0</asciidoctor-maven-plugin>
<jacoco-maven-plugin>0.8.13</jacoco-maven-plugin>
<sonar-maven-plugin>5.0.0.4389</sonar-maven-plugin>
```

Sonatype Publishing Plugin tauschen:
```xml
<!-- ALT: nexus-staging-maven-plugin:1.6.7 (Sonatype OSSRH legacy) -->
<!-- NEU: -->
<plugin>
    <groupId>org.sonatype.central</groupId>
    <artifactId>central-publishing-maven-plugin</artifactId>
    <version>0.7.0</version>
</plugin>
```

---

## Phase 6 – Camunda Update

**Dauer: ~1–2 Wochen**

```xml
<camunda.version>7.24.0</camunda.version>
```

Camunda 7 ist innerhalb der Major-Version weitgehend kompatibel. Der Sprung von 7.15
auf 7.24 umfasst interne Verbesserungen und Bugfixes.

**Langfristige Strategie:** Camunda 7 CE wird keine weiteren Releases nach 7.24.0
erhalten. Optionen evaluieren:
- Migration auf **Camunda 8** (vollständig neue cloud-native Architektur, kein Drop-in)
- Wechsel zu **Flowable** oder **Activiti** (offene Forks mit aktiver Community)

---

## Modulreihenfolge für die Migration

Die Build-Reihenfolge ergibt sich aus den Abhängigkeiten. Innerhalb jeder Phase
in dieser Reihenfolge arbeiten:

```
1. appng-tools          (keine appng-Deps)
2. appng-mail           (keine appng-Deps)
3. appng-forms          (Servlet/Tomcat-abhängig)
4. appng-xmlapi         ← JAXB-Fundament (Phase 2 beginnt hier)
5. appng-appngizer-jaxb ← JAXB-Modul 2
6. appng-formtags       (appng-tools, appng-forms, appng-mail)
7. appng-api            (appng-forms, appng-xmlapi, appng-tools)
8. appng-rest-api       (OpenAPI-generiert)
9. appng-persistence    (Spring, Hibernate, JPA, QueryDSL)
10. appng-search        (Lucene, Tika, POI, PDFBox)
11. appng-core          (alles oben, größtes Modul)
12. appng-cli           (appng-core)
13. appng-taglib        (appng-core, appng-search, appng-formtags)
14. appng-appngizer     (appng-appngizer-jaxb, appng-core)
15. appng-camunda       (appng-api, Camunda)
16. appng-application   (WAR: alle Core-Module)
17. appng-application-camunda (WAR: appng-application + appng-camunda)
18. appng-upngizr       (WAR: eigenständig)
```

---

## Gesamtzeitplan

| Phase | Inhalt | Dauer |
|---|---|---|
| **0** | Sofortmaßnahmen: Log4j entfernen, Tomcat-Patch, Trivial-Updates | 1 Woche |
| **1** | Java 17, SLF4J 2.x, MySQL/HikariCP/Flyway/POI/PDFBox | 2 Wochen |
| **2** | JAXB-Migration (appng-xmlapi zuerst, dann appngizer-jaxb, appng-core) | 1–2 Wochen |
| **3** | Tika 3.x, Lucene 9.x (→ 10.x nach Phase 5) | 2 Wochen |
| **4** | Jakarta EE Migration: Spring 7, Hibernate 7, Tomcat 11 | 4–6 Wochen |
| **5** | Java 21, Lucene 10, Maven-Plugins finalisieren | 1–2 Wochen |
| **6** | Camunda 7.15 → 7.24 | 1–2 Wochen |
| **Gesamt** | | **~12–16 Wochen** |

---

## Risikomatrix

| Risiko | Wahrscheinlichkeit | Wirkung | Maßnahme |
|---|---|---|---|
| Hibernate Legacy Criteria-Nutzung | **Hoch** | **Hoch** | Vor Phase 4: `grep -r "createCriteria"` |
| Lucene-Index-Inkompatibilität | **Sehr hoch** | **Hoch** | Re-Index nach Phase 3 und 5 einplanen |
| Spring Security Config-Umbau | **Hoch** | **Hoch** | Frühzeitig `WebSecurityConfigurerAdapter` identifizieren |
| Keycloak 15 → 25 Breaking Changes | **Sehr hoch** | **Mittel** | Separat planen, ggf. Keycloak-Adapter neu schreiben |
| QueryDSL Q-Klassen nach Hibernate-Wechsel | **Hoch** | **Mittel** | `mvn clean generate-sources` nach Hibernate-Upgrade |
| JAXB bindings.xml Kompatibilität | **Mittel** | **Mittel** | bindings.xml auf Jakarta-Namespace prüfen |
| Flyway Clean in Tests kaputt | **Mittel** | **Niedrig** | `flyway.cleanDisabled=false` in Testprofil |
| Prometheus-Client API-Wechsel | **Hoch** | **Mittel** | Auf Micrometer migrieren |
| commons-fileupload Nutzung | **Mittel** | **Mittel** | Alle `CommonsMultipartResolver` finden |

---

## Voraussetzungen vor dem Start

1. **Testabdeckung prüfen:** Integrationstests für kritische Pfade vorhanden? Fehlende jetzt ergänzen.
2. **Datenbank-Backup:** Vor Phase 4 wegen Hibernate-Identifier-Strategieänderungen.
3. **Staging-Umgebung:** Alle Phasen dort testen, insbesondere Flyway-Migrationen.
4. **Dedizierter Branch:** `feature/jakarta-migration` für Phase 4.
5. **CI/CD anpassen:** Jenkins/CI auf Java 17 (Phase 1), dann Java 21 (Phase 5) umstellen.
6. **Team-Alignment:** Alle Entwickler müssen wissen, dass `javax.*`-Importe ab Phase 4 verboten sind.
