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
package org.appng.persistence.hibernate.dialect;

import java.sql.Types;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.sql.internal.CapacityDependentDdlType;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

/**
 * A {@link MariaDBDialect} which converts varchar-fields ({@link Types#VARCHAR}) with
 * <ul>
 * <li>a length of <= 1000 to type <a href="https://mariadb.com/kb/en/varchar/">varchar</a>
 * <li>a length of <= 16383 to type <a href="https://mariadb.com/kb/en/text/">text</a>
 * <li>a length of <= 4194303 to type <a href="https://mariadb.com/kb/en/mediumtext/">mediumtext</a>
 * <li>a length of > 4194303 to type <a href="https://mariadb.com/kb/en/longtext/">longtext</a>
 * </ul>
 * These values assume <a href="https://mariadb.com/kb/en/supported-character-sets-and-collations">utf8mb4</a> character
 * encoding, where each character consumes up to 4 bytes.<br/>
 *
 * @author Matthias Müller
 */
public class MariaDB103DialectUTF8 extends MariaDBDialect {

	@Override
	protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		super.registerColumnTypes(typeContributions, serviceRegistry);
		final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();
		ddlTypeRegistry.addDescriptor(
			CapacityDependentDdlType.builder(Types.VARCHAR, "longtext", this)
				.withTypeCapacity(4194303, "mediumtext")
				.withTypeCapacity(16383, "text")
				.withTypeCapacity(1000, "varchar($l)")
				.build()
		);
	}
}
