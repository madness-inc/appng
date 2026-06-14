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
package org.appng.api.support;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

import org.springframework.core.SmartClassLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SiteClassLoader extends URLClassLoader implements SmartClassLoader {

	private final String site;

	public SiteClassLoader(URL[] urls, ClassLoader parent, String site) {
		super(urls, parent);
		this.site = site;
		LOGGER.info("{} created", this);
	}

	public SiteClassLoader(String site) {
		this(new URL[0], SiteClassLoader.class.getClassLoader(), site);
	}

	@Override
	/* for simpler debugging */
	public void close() throws IOException {
		super.close();
		ResourceBundle.clearCache(this);
	}

	// avoids caching of cglib proxy classes
	public boolean isClassReloadable(Class<?> clazz) {
		return true;
	}

	public String getSiteName() {
		return site;
	}

	@Override
	public String toString() {
		return "SiteClassLoader#" + hashCode() + " for site " + site + " with parent "
				+ getParent().getClass().getName() + "#" + getParent().hashCode();
	}

}
