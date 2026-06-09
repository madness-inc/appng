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
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.sql.internal.CapacityDependentDdlType;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;

/**
 * A {@link org.hibernate.dialect.HSQLDialect} which converts varchar-fields with a length of >=1024 to hsql-type
 * 'LONGVARCHAR'
 *
 * @author Matthias Müller
 */
public class HSQLDialect extends org.hibernate.dialect.HSQLDialect {

	private static final long MAX_LENGTH = 1024;

	@Override
	protected void registerColumnTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		super.registerColumnTypes(typeContributions, serviceRegistry);
		final DdlTypeRegistry ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();
		ddlTypeRegistry.addDescriptor(
			CapacityDependentDdlType.builder(Types.VARCHAR, "LONGVARCHAR", this)
				.withTypeCapacity(MAX_LENGTH - 1, "varchar($l)")
				.build()
		);
	}
}
