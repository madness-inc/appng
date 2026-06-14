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
package org.appng.core.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebAppRootListener;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * A {@link ServletContextListener} that sets the {@code webapp.root} system property (via {@link WebAppRootListener})
 * and then configures Logback from {@code WEB-INF/conf/logback.xml} using Joran.
 * This order ensures that {@code ${webapp.root}} is already resolved when the file appenders are initialized.
 *
 * @author Matthias Müller
 */
public class Log4jConfigurer extends WebAppRootListener {

	public static final String WEB_INF = "/WEB-INF";
	private static final String LOGBACK_XML = WEB_INF + "/conf/logback.xml";

	public void contextInitialized(ServletContextEvent sce) {
		// 1. webapp.root als System-Property setzen (wird in logback.xml als ${webapp.root} referenziert)
		super.contextInitialized(sce);

		// 2. Logback via Joran aus WEB-INF/conf/logback.xml ( neu) konfigurieren
		String configFile = sce.getServletContext().getRealPath(LOGBACK_XML);
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(loggerContext);
			loggerContext.reset();
			configurator.doConfigure(configFile);
		} catch (JoranException e) {
			// StatusPrinter gibt Details auf stderr aus
			ch.qos.logback.core.util.StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
		}

		Logger logger = LoggerFactory.getLogger(Log4jConfigurer.class);
		logger.info("Logging configured from {}", configFile);
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
