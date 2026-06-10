/*
 * Copyright 2011-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.appng.core.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.appng.core.domain.SiteImpl;
import org.appng.persistence.hibernate.dialect.HSQLDialect;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Generates {@code V1_0__appNG_schema.sql} per database type from the current JPA entity model.
 * All existing scripts except {@code V1_1__Quartz_schema.sql} are replaced.
 * Run manually with Docker available.
 */
public class SchemaExportTest {

	private static final String MIGRATION_BASE = "src/main/resources/db/migration";

	@Test
	@Ignore("Generates Flyway migration scripts - requires Docker")
	public void exportSchemas() throws Exception {
		exportSchema("hsql", HSQLDialect.class.getName(),
				"jdbc:hsqldb:mem:schema-export", "org.hsqldb.jdbc.JDBCDriver", "sa", "");

		try (MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")) {
			mysql.start();
			exportSchema("mysql", MySQLDialect.class.getName(),
					mysql.getJdbcUrl(), mysql.getDriverClassName(), mysql.getUsername(), mysql.getPassword());
		}

		try (PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:17")) {
			pg.start();
			exportSchema("postgresql", PostgreSQLDialect.class.getName(),
					pg.getJdbcUrl(), pg.getDriverClassName(), pg.getUsername(), pg.getPassword());
		}

		try (MSSQLServerContainer<?> mssql = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
				.acceptLicense()) {
			mssql.start();
			exportSchema("mssql", SQLServerDialect.class.getName(),
					mssql.getJdbcUrl(), mssql.getDriverClassName(), mssql.getUsername(), mssql.getPassword());
		}
	}

	private void exportSchema(String dbType, String dialectClass, String jdbcUrl,
			String driverClass, String user, String password) throws Exception {

		File tmpFile = File.createTempFile("appng-schema-" + dbType + "-", ".sql");
		try {
			DriverManagerDataSource ds = new DriverManagerDataSource(jdbcUrl, user, password);
			ds.setDriverClassName(driverClass);

			Properties props = new Properties();
			props.put("hibernate.dialect", dialectClass);
			props.put("jakarta.persistence.schema-generation.scripts.action", "create");
			props.put("jakarta.persistence.schema-generation.scripts.create-target", tmpFile.getAbsolutePath());
			props.put("jakarta.persistence.schema-generation.database.action", "none");
			props.put("hibernate.hbm2ddl.delimiter", ";");
			props.put("hibernate.format_sql", "true");
			props.put("hibernate.physical_naming_strategy", PhysicalNamingStrategySnakeCaseImpl.class.getName());

			LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
			emfb.setDataSource(ds);
			emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			emfb.setPackagesToScan(SiteImpl.class.getPackage().getName());
			emfb.setJpaProperties(props);
			emfb.setPersistenceUnitName("schema-export-" + dbType);
			emfb.afterPropertiesSet();
			emfb.destroy();

			String jpaSql = Files.readString(tmpFile.toPath(), StandardCharsets.UTF_8);

			Path dir = Paths.get(MIGRATION_BASE, dbType);
			replaceSchema(dir, formatSql(jpaSql));
			System.out.printf("Written: %s/V1_0__appNG_schema.sql%n", dir);
		} finally {
			tmpFile.delete();
		}
	}

	private void replaceSchema(Path dir, String content) throws Exception {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.sql")) {
			for (Path p : stream) {
				String name = p.getFileName().toString();
				if (name.equals("V1_0__appNG_schema.sql")) {
					Files.delete(p);
				}
			}
		}
		Files.writeString(dir.resolve("V1_0__appNG_schema.sql"), content, StandardCharsets.UTF_8);
	}

	private static final List<String> DDL_KEYWORDS = Arrays.asList(
			"create table", "create unique index", "create index",
			"alter table", "drop table", "drop index",
			"add constraint", "primary key", "foreign key",
			"not null", "references", "constraint", "unique");

	private String formatSql(String sql) {
		// Remove any Hibernate-generated block comments
		sql = sql.replaceAll("(?s)/\\*.*?\\*/\\s*", "");
		// Remove standalone comment lines
		sql = sql.replaceAll("(?m)^\\s*--.*$", "");
		// Uppercase DDL keywords
		for (String kw : DDL_KEYWORDS) {
			sql = sql.replaceAll("(?i)\\b" + Pattern.quote(kw) + "\\b", kw.toUpperCase());
		}
		// Collapse multiple blank lines into one
		sql = sql.replaceAll("\n{3,}", "\n\n");
		return sql.trim();
	}

}
