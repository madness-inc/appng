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
package org.appng.api.config;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.appng.api.Environment;
import org.appng.api.model.Application;
import org.appng.api.model.Site;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link Configuration}that adds a {@link MappingJackson2HttpMessageConverter} and an {@link ObjectMapper} to the
 * context, if not already present. <br/>
 * Also checks the context for Jackson {@link JacksonModule}s and adds them to the {@link JsonMapper}.<br/>
 * Additionally, modules for handling these {@link Temporal}-types are registered:
 * <ul>
 * <li>{@link OffsetDateTime}, using {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME}
 * <li>{@link LocalDate}, using {@link DateTimeFormatter#ISO_LOCAL_DATE}
 * <li>{@link LocalTime}, using {@link DateTimeFormatter#ISO_LOCAL_TIME}
 * <li>{@link LocalDateTime}, using {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
 * </ul>
 * <br/>
 * Also adds a {@link HandlerMethodArgumentResolver} that can resolve the current {@link Environment}, {@link Site} and
 * {@link Application}.
 * 
 * @author Matthias Müller
 */
@Slf4j
@Configuration
public class RestConfig implements BeanFactoryPostProcessor {

	private static final String DEFAULT_JACKSON_CONVERTER = "defaultJacksonConverter";
	private static final String DEFAULT_OBJECT_MAPPER = "defaultObjectMapper";

	public static List<HttpMessageConverter<?>> getMessageConverters(ApplicationContext context) {
		return context.getBeansOfType(HttpMessageConverter.class).values().stream()
				.map(m -> (HttpMessageConverter<?>) m).collect(Collectors.toList());
	}

	public static List<HandlerMethodArgumentResolver> getArgumentResolvers(ApplicationContext context) {
		return context.getBeansOfType(HandlerMethodArgumentResolver.class).values().stream()
				.collect(Collectors.toList());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Site site = beanFactory.getBean(Site.class);
		Application app = beanFactory.getBean(Application.class);
		String siteApp = String.format("[%s:%s]", site.getName(), app.getName());
		Boolean jsonPrettyPrint = site.getProperties().getBoolean("jsonPrettyPrint", true);
		Map<String, JacksonJsonHttpMessageConverter> jacksonConverters = beanFactory
				.getBeansOfType(JacksonJsonHttpMessageConverter.class);
		LOGGER.info("{} Found {} JacksonJsonHttpMessageConverters: {}", siteApp, jacksonConverters.size(),
				StringUtils.join(jacksonConverters.keySet(), ", "));

		Map<String, JsonMapper> jsonMappers = beanFactory.getBeansOfType(JsonMapper.class);
		LOGGER.info("{} Found {} JsonMappers: {}", siteApp, jsonMappers.size(),
				StringUtils.join(jsonMappers.keySet(), ", "));

		Map<String, JacksonModule> modules = beanFactory.getBeansOfType(JacksonModule.class);
		LOGGER.info("{} Found {} Modules: {}", siteApp, modules.size(), StringUtils.join(modules.keySet(), ", "));

		Map<String, Object> primaryBeans = beanFactory.getBeansWithAnnotation(Primary.class);
		LOGGER.info("{} Found {} @Primary Beans: {}", siteApp, primaryBeans.size(),
				StringUtils.join(primaryBeans.keySet(), ", "));

		boolean registerObjectMapper = false;
		JsonMapper.Builder mapperBuilder;
		if (registerObjectMapper = jsonMappers.isEmpty()) {
			mapperBuilder = JsonMapper.builder()
					.changeDefaultPropertyInclusion(v -> JsonInclude.Value.ALL_NON_ABSENT)
					.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
			LOGGER.info("{} No JsonMapper found in context, creating default.", siteApp);
		} else {
			mapperBuilder = getPrimaryOrFirst(jsonMappers, primaryBeans).rebuild();
		}
		if (jsonPrettyPrint) {
			mapperBuilder.enable(SerializationFeature.INDENT_OUTPUT);
		}

		addDateModules(mapperBuilder);
		for (Entry<String, JacksonModule> moduleEntry : modules.entrySet()) {
			mapperBuilder.addModule(moduleEntry.getValue());
			LOGGER.info("{} Adding Module '{}' to JsonMapper", siteApp, moduleEntry.getKey());
		}

		JsonMapper mapper = mapperBuilder.build();

		boolean registerConverter = false;
		JacksonJsonHttpMessageConverter converter;
		if (registerConverter = jacksonConverters.isEmpty()) {
			converter = new JacksonJsonHttpMessageConverter(mapper);
			LOGGER.info("{} No JacksonJsonHttpMessageConverter found in context, creating default.", siteApp);
		} else {
			converter = getPrimaryOrFirst(jacksonConverters, primaryBeans);
		}

		if (registerObjectMapper) {
			beanFactory.registerSingleton(DEFAULT_OBJECT_MAPPER, mapper);
			LOGGER.info("{} Registering JsonMapper '{}'", siteApp, DEFAULT_OBJECT_MAPPER);
		}

		if (registerConverter) {
			beanFactory.registerSingleton(DEFAULT_JACKSON_CONVERTER, converter);
			LOGGER.info("{} Registering JacksonJsonHttpMessageConverter '{}'", siteApp, DEFAULT_JACKSON_CONVERTER);
		}
	}

	protected <T> T getPrimaryOrFirst(Map<String, T> beans, Map<String, Object> primaryBeans) {
		T bean;
		Optional<Entry<String, T>> entry = beans.entrySet().stream().filter(e -> primaryBeans.containsKey(e.getKey()))
				.findFirst();
		boolean isPrimary = false;
		if (isPrimary = entry.isPresent()) {
			bean = entry.get().getValue();
		} else {
			entry = Optional.of(beans.entrySet().iterator().next());
			bean = entry.get().getValue();
		}
		LOGGER.info("Found {} '{}'", (isPrimary ? "@Primary " : "") + entry.get().getValue().getClass().getName(),
				entry.get().getKey());
		return bean;
	}

	// @formatter:off
	protected void addDateModules(JsonMapper.Builder mapperBuilder) {
		mapperBuilder
			.addModule(getDateModule(OffsetDateTime.class, OffsetDateTime::parse, DateTimeFormatter.ISO_OFFSET_DATE_TIME))
			.addModule(getDateModule(LocalDate.class, LocalDate::parse, DateTimeFormatter.ISO_LOCAL_DATE))
			.addModule(getDateModule(LocalTime.class, LocalTime::parse, DateTimeFormatter.ISO_LOCAL_TIME))
			.addModule(getDateModule(LocalDateTime.class, LocalDateTime::parse, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}
	// @formatter:on

	protected <T extends Temporal> SimpleModule getDateModule(Class<T> temporal, Function<String, T> parseFunction,
			DateTimeFormatter formatter) {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(temporal, new ValueDeserializer<T>() {
			@Override
			public T deserialize(JsonParser parser, DeserializationContext ctxt) throws JacksonException {
				if (StringUtils.isNotBlank(parser.getText())) {
					return parseFunction.apply(parser.getText());
				}
				return null;
			}
		});
		module.addSerializer(temporal, new ValueSerializer<T>() {
			@Override
			public void serialize(T value, JsonGenerator jsonGenerator, SerializationContext provider)
					throws JacksonException {
				if (value != null) {
					jsonGenerator.writeString(formatter.format(value));
				}
			}
		});
		LOGGER.debug("Added Module handling {}.", temporal.getName());
		return module;
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		return new ByteArrayHttpMessageConverter();
	}

	@Bean
	@Lazy
	@RequestScope(proxyMode = ScopedProxyMode.NO)
	public SiteAwareHandlerMethodArgumentResolver siteAwareHandlerMethodArgumentResolver(Site site,
			Application application, Environment environment) {
		return new SiteAwareHandlerMethodArgumentResolver(site, environment, application);
	}

	/**
	 * A {@link HandlerMethodArgumentResolver} that can resolve the current {@link Application}, {@link Environment} and
	 * {@link Site}.
	 */
	@AllArgsConstructor
	public static class SiteAwareHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

		private final Site site;
		private final Environment environment;
		private final Application application;

		public boolean supportsParameter(MethodParameter parameter) {
			return isSite(parameter) || isEnvironment(parameter) || isApplication(parameter);
		}

		public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
				NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
			return isSite(parameter) ? site
					: (isEnvironment(parameter) ? environment : (isApplication(parameter) ? application : null));
		}

		private boolean isEnvironment(MethodParameter parameter) {
			return isParameterType(parameter, Environment.class);
		}

		protected boolean isSite(MethodParameter parameter) {
			return isParameterType(parameter, Site.class);
		}

		private boolean isApplication(MethodParameter parameter) {
			return isParameterType(parameter, Application.class);
		}

		private boolean isParameterType(MethodParameter parameter, Class<?> type) {
			return parameter.getParameterType().equals(type);
		}

	}

}
