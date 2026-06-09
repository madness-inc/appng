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

import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.sql.internal.CapacityDependentDdlType;
import org.junit.Assert;
import org.junit.Test;

public class DialectUTF8Test {

	@Test
	public void testMariaDB103() {
		runTest(new MariaDB103DialectUTF8());
	}

	@Test
	public void testMySql57() {
		runTest(new MySql57DialectUTF8());
	}

	@Test
	public void testMySql8() {
		runTest(new MySql8DialectUTF8());
	}

	private void runTest(Dialect dialect) {
		CapacityDependentDdlType ddlType = CapacityDependentDdlType.builder(Types.VARCHAR, "longtext", dialect)
			.withTypeCapacity(4194303, "mediumtext")
			.withTypeCapacity(16383, "text")
			.withTypeCapacity(1000, "varchar($l)")
			.build();
		Assert.assertEquals("varchar(255)", ddlType.getTypeName(255L, null, null));
		Assert.assertEquals("varchar(1000)", ddlType.getTypeName(1000L, null, null));
		Assert.assertEquals("text", ddlType.getTypeName(1001L, null, null));
		Assert.assertEquals("text", ddlType.getTypeName(16383L, null, null));
		Assert.assertEquals("mediumtext", ddlType.getTypeName(16384L, null, null));
		Assert.assertEquals("mediumtext", ddlType.getTypeName(4194303L, null, null));
		Assert.assertEquals("longtext", ddlType.getTypeName(4194304L, null, null));
	}
}
